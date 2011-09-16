/**
 * Copyright (C) 2010, 2011 by Arne Kesting, Martin Treiber,
 *                             Ralph Germ, Martin Budden
 *                             <info@movsim.org>
 * ----------------------------------------------------------------------
 * 
 *  This file is part of 
 *  
 *  MovSim - the multi-model open-source vehicular-traffic simulator 
 *
 *  MovSim is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  MovSim is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with MovSim.  If not, see <http://www.gnu.org/licenses/> or
 *  <http://www.movsim.org>.
 *  
 * ----------------------------------------------------------------------
 */
package org.movsim.simulator.vehicles.lanechanging.impl;

import java.util.List;

import org.movsim.input.model.vehicle.laneChanging.LaneChangingInputData;
import org.movsim.simulator.Constants;
import org.movsim.simulator.vehicles.Vehicle;
import org.movsim.simulator.vehicles.VehicleContainer;
import org.movsim.simulator.vehicles.lanechanging.LaneChangingModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class LaneChangingModelImpl.
 */
public class LaneChangingModelImpl implements LaneChangingModel {

    /** The Constant logger. */
    final static Logger logger = LoggerFactory.getLogger(LaneChangingModelImpl.class);

    // to avoid flips:
    public static double LANECHANGE_TDELAY_S = 3.0; // delay nach Spurwechsel
    public static double LANECHANGE_TDELAY_FRONT_S = 3.0; // delay nach

    private final boolean withEuropeanRules;

    // critical speed for kicking in European rules (in m/s)
    private final double vCritEur;

    private int mandatoryChange = Constants.NO_CHANGE;

    private Vehicle me;

    private final boolean isInitialized;

    private MOBILImpl lcModelMOBIL;

    private final LaneChangingInputData lcInputData;

    /**
     * Instantiates a new lane changing model impl.
     *
     * @param lcInputData the lc input data
     */
    public LaneChangingModelImpl(LaneChangingInputData lcInputData) {

        this.lcInputData = lcInputData;
        this.withEuropeanRules = lcInputData.isWithEuropeanRules();
        this.vCritEur = lcInputData.getCritSpeedEuroRules();

        // TODO valid lane change model only if configured by xml
        isInitialized = lcInputData.isInitializedMobilData();

    }

    /**
     * Initialize.
     *
     * @param vehicle the vehicle
     */
    public void initialize(final Vehicle vehicle) {
        this.me = vehicle;
        lcModelMOBIL = (isInitialized) ? new MOBILImpl(me, lcInputData.getLcMobilData()) : new MOBILImpl(me);
    }

    /**
     * Checks if is initialized.
     *
     * @return true, if is initialized
     */
    public boolean isInitialized() {
        return isInitialized;
    }

    @Override
    public boolean isMandatoryLaneChangeSafe(final VehicleContainer vehContainerTargetLane) {
        // works also for the "virtual" leader of me in considered lane
        final Vehicle frontMain = vehContainerTargetLane.getLeader(me);

        final Vehicle backMain = vehContainerTargetLane.getFollower(me);

        final boolean changeSafe = mandatoryWeavingChange(frontMain, backMain); // TODO
        return changeSafe;
    }

    private boolean mandatoryWeavingChange(final Vehicle frontVeh, final Vehicle backVeh) {

        // safety incentive (in two steps)
        final double gapFront = me.getNetDistance(frontVeh);
        final double gapBack = (backVeh == null) ? Constants.GAP_INFINITY : backVeh.getNetDistance(me);

        // check distances
        // negative net distances possible because of different veh lengths!
        if (gapFront < lcModelMOBIL.getMinimumGap() || gapBack < lcModelMOBIL.getMinimumGap()) {
            logger.debug("gapFront={}, gapBack={}", gapFront, gapBack);
            return false;
        }

        final double backNewAcc = (backVeh == null) ? 0 : backVeh.getAccelerationModel().calcAcc(backVeh, me);

        // check security constraint for new follower
        // enforce mandatory lane change by increasing the safe deceleration for
        // normal situations
        final double increaseFactorMandatory = 2.0;
        if (backNewAcc <= -increaseFactorMandatory * lcModelMOBIL.getSafeDeceleration()) {
            logger.debug("gapFront = {}, gapBack = {}", gapFront, gapBack);
            logger.debug("backNewAcc={}, bSafe={}", backNewAcc, lcModelMOBIL.getSafeDeceleration());
            return (false);
        }

        final double meNewAcc = me.getAccelerationModel().calcAcc(me, frontVeh);
        if (meNewAcc >= -increaseFactorMandatory * lcModelMOBIL.getSafeDeceleration()) {
            logger.debug("meNewAcc={}, bSafe={}", meNewAcc, lcModelMOBIL.getSafeDeceleration());
            logger.debug("gapFront={}, gapBack={}", gapFront, gapBack);
            logger.debug("backNewAcc={}, bSafe={}", backNewAcc, lcModelMOBIL.getSafeDeceleration());
            return (true);
        }
        return (false);
    }

    public int determineLaneChangingDirection(final List<VehicleContainer> vehContainers) {

        final int currentLane = me.getLane();

        // initialize with largest possible deceleration
        double accToLeft = -Double.MAX_VALUE;
        double accToRight = -Double.MAX_VALUE;

        // consider lane-changing to right-hand side lane (decreasing lane
        // index)
        if (currentLane - 1 >= Constants.MOST_RIGHT_LANE) {
            accToRight = lcModelMOBIL.calcAccelerationBalance(Constants.TO_RIGHT, vehContainers);
        }

        // consider lane-changing to left-hand side lane (increasing the lane
        // index)
        if (currentLane + 1 < vehContainers.size()) {
            accToLeft = lcModelMOBIL.calcAccelerationBalance(Constants.TO_LEFT, vehContainers);
        }

        // decision
        if ((accToRight > 0) || (accToLeft > 0)) {
            logger.debug("accToRight={}, accToLeft={}", accToRight, accToLeft);
            logger.debug("currentLane={}", currentLane);
            if (accToRight > accToLeft) {
                return Constants.TO_RIGHT;
            } else {
                return Constants.TO_LEFT;
            }
        }

        return Constants.NO_CHANGE;
    }

    public void setMandatoryChange(int incentive) {
        if (incentive == Constants.NO_CHANGE || incentive == Constants.TO_RIGHT || incentive == Constants.TO_LEFT) {
            mandatoryChange = incentive;
            System.out.println("LaneChange.setMandatoryChange:" + " mandatoryChange= " + mandatoryChange);
        } else {
            System.exit(-1); // debugging
        }
    }

    public boolean withEuropeanRules() {
        return withEuropeanRules;
    }

    public double vCritEurRules() {
        return vCritEur;
    }

}
