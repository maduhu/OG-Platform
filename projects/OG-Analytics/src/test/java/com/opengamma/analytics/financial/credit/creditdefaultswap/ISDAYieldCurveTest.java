/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.credit.creditdefaultswap;

import org.testng.annotations.Test;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.ZonedDateTime;

import com.opengamma.analytics.financial.credit.isdayieldcurve.ISDAInstrumentTypes;
import com.opengamma.analytics.financial.credit.isdayieldcurve.ISDAYieldCurve;
import com.opengamma.analytics.financial.credit.schedulegeneration.GenerateCreditDefaultSwapPremiumLegSchedule;
import com.opengamma.analytics.util.time.TimeCalculator;
import com.opengamma.financial.convention.businessday.BusinessDayConvention;
import com.opengamma.financial.convention.businessday.BusinessDayConventionFactory;
import com.opengamma.financial.convention.calendar.Calendar;
import com.opengamma.financial.convention.calendar.MondayToFridayCalendar;
import com.opengamma.financial.convention.daycount.DayCount;
import com.opengamma.financial.convention.daycount.DayCountFactory;
import com.opengamma.financial.convention.frequency.PeriodFrequency;

/**
 * 
 */
public class ISDAYieldCurveTest {

  // ----------------------------------------------------------------------------------------------------------------------------------------

  // Daycount conventions

  private static final DayCount ACT_365 = DayCountFactory.INSTANCE.getDayCount("ACT/365");
  private static final DayCount ACT_365F = DayCountFactory.INSTANCE.getDayCount("ACT/365F");
  private static final DayCount ACT_360 = DayCountFactory.INSTANCE.getDayCount("ACT/360");

  // ----------------------------------------------------------------------------------------------------------------------------------------

  private static final int spotDays = 2;

  // This is the valuation date
  private static final ZonedDateTime baseDate = zdt(2012, 11, 15, 0, 0, 0, 0, ZoneOffset.UTC);

  // This is the anchor date for the curve - Z(basedate) = 1
  final ZonedDateTime spotDate = zdt(2012, 11, 19, 0, 0, 0, 0, ZoneOffset.UTC);

  // This is the anchor date for the curve - Z(basedate) = 1, valuationDate + spotDays (bda)

  // The MM dates
  // TODO : Need to calc these automatically - they are bda
  private final static ZonedDateTime[] dates = {
      zdt(2012, 12, 17, 0, 0, 0, 0, ZoneOffset.UTC),   // 1M, MM
      zdt(2013, 1, 15, 0, 0, 0, 0, ZoneOffset.UTC),    // 2M, MM
      zdt(2013, 2, 15, 0, 0, 0, 0, ZoneOffset.UTC),    // 3M, MM
      zdt(2013, 5, 15, 0, 0, 0, 0, ZoneOffset.UTC),    // 6M, MM
      zdt(2013, 8, 15, 0, 0, 0, 0, ZoneOffset.UTC)     // 9M, MM
  };

  /*  
    ,    // MM
    zdt(2014, 11, 15, 0, 0, 0, 0, TimeZone.UTC),   // Swap
    zdt(2015, 11, 15, 0, 0, 0, 0, TimeZone.UTC),
    zdt(2016, 11, 15, 0, 0, 0, 0, TimeZone.UTC),
    zdt(2017, 11, 15, 0, 0, 0, 0, TimeZone.UTC),
    zdt(2018, 11, 15, 0, 0, 0, 0, TimeZone.UTC),
    zdt(2019, 11, 17, 0, 0, 0, 0, TimeZone.UTC),
    zdt(2020, 11, 15, 0, 0, 0, 0, TimeZone.UTC),
    zdt(2021, 11, 15, 0, 0, 0, 0, TimeZone.UTC),
    zdt(2022, 11, 15, 0, 0, 0, 0, TimeZone.UTC),
    zdt(2024, 11, 17, 0, 0, 0, 0, TimeZone.UTC),
    zdt(2027, 11, 15, 0, 0, 0, 0, TimeZone.UTC),
    zdt(2032, 11, 15, 0, 0, 0, 0, TimeZone.UTC),
    zdt(2037, 11, 15, 0, 0, 0, 0, TimeZone.UTC),
    zdt(2042, 11, 16, 0, 0, 0, 0, TimeZone.UTC)
  };

   */

  private static final double[] rates = {
      0.002075,                 // MM
      0.0025699999999999998,    // MM
      0.00310999999999,         // MM
      0.0052300000000000003,    // MM
      0.0069649999999999998     // MM
  };

  /*  ,
    0.00377,
    0.00451,
    0.005834,
    0.007625,
    0.009617,
    0.011546,
    0.01329,
    0.01488,
    0.016383,
    0.018786,
    0.02122,
    0.023181,
    0.024195,
    0.02481
  };

   */

  // The instrument types
  private static final ISDAInstrumentTypes[] rateTypes = {
      ISDAInstrumentTypes.MoneyMarket,
      ISDAInstrumentTypes.MoneyMarket,
      ISDAInstrumentTypes.MoneyMarket,
      ISDAInstrumentTypes.MoneyMarket,
      ISDAInstrumentTypes.MoneyMarket
  };

  /*  ,
    ISDAInstrumentTypes.Swap,
    ISDAInstrumentTypes.Swap,
    ISDAInstrumentTypes.Swap,
    ISDAInstrumentTypes.Swap,
    ISDAInstrumentTypes.Swap,
    ISDAInstrumentTypes.Swap,
    ISDAInstrumentTypes.Swap,
    ISDAInstrumentTypes.Swap,
    ISDAInstrumentTypes.Swap,
    ISDAInstrumentTypes.Swap,
    ISDAInstrumentTypes.Swap,
    ISDAInstrumentTypes.Swap,
    ISDAInstrumentTypes.Swap,
    ISDAInstrumentTypes.Swap
  };
   */

  // ----------------------------------------------------------------------------------------------------------------------------------------

  private static final Calendar calendar = new MondayToFridayCalendar("TestCalendar");    // Holidays (None)

  private static final PeriodFrequency swapFixedLegCouponFrequency = PeriodFrequency.SEMI_ANNUAL;
  private static final PeriodFrequency swapFloatingLegCouponFrequency = PeriodFrequency.QUARTERLY;

  private static final DayCount moneyMarketDaycountFractionConvention = DayCountFactory.INSTANCE.getDayCount("ACT/360");              // MM DCC

  private static final DayCount swapFixedLegDaycountFractionConvention = DayCountFactory.INSTANCE.getDayCount("30/360");          // Swap DCC
  private static final DayCount swapFloatingLegDaycountFractionConvention = DayCountFactory.INSTANCE.getDayCount("ACT/360");      // Swap DCC

  private static final BusinessDayConvention businessdayAdjustmentConvention = BusinessDayConventionFactory.INSTANCE.getBusinessDayConvention("Following"); // Bad Day Conv

  private static final ISDAYieldCurve isdaYieldCurve = new ISDAYieldCurve(
      baseDate,
      dates,
      rateTypes,
      rates,
      spotDays,
      moneyMarketDaycountFractionConvention,
      swapFixedLegDaycountFractionConvention,
      swapFloatingLegDaycountFractionConvention,
      swapFixedLegCouponFrequency,
      swapFloatingLegCouponFrequency,
      businessdayAdjustmentConvention,
      calendar);

  // ----------------------------------------------------------------------------------------------------------------------------------------

  @Test
  public void testMMBuild() {

    for (long i = 0; i < 1500; i++)
    {
      ZonedDateTime testDate = baseDate.plusDays(i);

      final double Z = isdaYieldCurve.getDiscountFactor(baseDate, testDate);

      //System.out.println("i = " + "\t" + i + "\t" + testDate + "\t" + Z);
    }
  }

  // ----------------------------------------------------------------------------------------------------------------------------------------

  private static int getNumberOfInstruments(
      ISDAInstrumentTypes[] rateTypes,
      ISDAInstrumentTypes rateType) {

    int nInstruments = 0;

    for (int i = 0; i < rateTypes.length; i++) {

      if (rateTypes[i] == rateType) {
        nInstruments++;
      }

    }

    return nInstruments;
  }

  // ----------------------------------------------------------------------------------------------------------------------------------------

  private static ZonedDateTime[] getInstrumentDates(
      ZonedDateTime[] instrumentMaturities,
      ISDAInstrumentTypes[] rateTypes,
      ISDAInstrumentTypes rateType,
      final int nInstruments) {

    int index = 0;

    ZonedDateTime[] instrumentDates = new ZonedDateTime[nInstruments];

    for (int i = 0; i < rateTypes.length; i++) {

      if (rateTypes[i] == rateType) {
        instrumentDates[index] = instrumentMaturities[i];
        index++;
      }

    }

    return instrumentDates;
  }

  // ----------------------------------------------------------------------------------------------------------------------------------------

  private static double[] getInstrumentRates(
      double[] instrumentRates,
      ISDAInstrumentTypes[] rateTypes,
      ISDAInstrumentTypes rateType,
      final int nInstruments) {

    int index = 0;

    double[] instrumentInputRates = new double[nInstruments];

    for (int i = 0; i < rateTypes.length; i++) {

      if (rateTypes[i] == rateType) {
        instrumentInputRates[index] = instrumentRates[i];
        index++;
      }

    }

    return instrumentInputRates;
  }

  // ----------------------------------------------------------------------------------------------------------------------------------------

  private static int getNumberOfActiveSwaps(
      ZonedDateTime lastStubDate,
      ZonedDateTime[] swapDates,
      final int nSwap) {

    int offset = 0;

    int numSwaps = nSwap;

    while (numSwaps > 0 && swapDates[offset].isBefore(lastStubDate)) {
      offset++;
      numSwaps--;
    }

    return numSwaps;
  }

  // ----------------------------------------------------------------------------------------------------------------------------------------

  boolean calculateOnCycle(
      ZonedDateTime valueDate,
      ZonedDateTime unadjustedSwapDate,
      PeriodFrequency swapFixedLegCouponFrequency) {

    boolean onCycle = false;

    if (valueDate.getDayOfMonth() <= 28 && unadjustedSwapDate.getDayOfMonth() <= 28) {

      ZonedDateTime fromDate = valueDate;
      ZonedDateTime toDate = unadjustedSwapDate;

      double intervalYears = 0.0;
      double fromToYears = TimeCalculator.getTimeBetween(fromDate, toDate, ACT_365);

      // Need to fix this - bit of a hack
      if (swapFixedLegCouponFrequency == PeriodFrequency.SEMI_ANNUAL) {
        intervalYears = 0.5;
      }

      if (swapFixedLegCouponFrequency == PeriodFrequency.QUARTERLY) {
        intervalYears = 0.25;
      }

      int lowNumIntervals = Math.max(0, (int) Math.floor(Math.abs(fromToYears / intervalYears)) - 2);
      int index = lowNumIntervals;

      int compoundInterval = 0;
      int multiplier = 0;

      // Need to fix this - bit of a hack
      if (swapFixedLegCouponFrequency == PeriodFrequency.SEMI_ANNUAL) {
        multiplier = 6;
        compoundInterval = index * multiplier;
      }

      if (swapFixedLegCouponFrequency == PeriodFrequency.QUARTERLY) {
        multiplier = 3;
        compoundInterval = index * multiplier;
      }

      ZonedDateTime currDate = valueDate.plusMonths(compoundInterval);
      ZonedDateTime lastDate = currDate;

      while (currDate.isAfter(fromDate) && !currDate.isAfter(toDate)) {
        ++index;
        lastDate = currDate;
        currDate = valueDate.plusMonths(index * multiplier);
      }

      int numIntervals = index - 1;
      int extraDays = (int) Math.abs(TimeCalculator.getTimeBetween(toDate, lastDate));

      if (extraDays == 0) {
        onCycle = true;
      }
    }

    return onCycle;
  }

  // ----------------------------------------------------------------------------------------------------------------------------------------

  // Assuming that the inputs are ordered as MM followed by swap instruments

  private void jpmCDSZCSwaps(
      ZonedDateTime valueDate,
      ZonedDateTime[] cashDates,
      ZonedDateTime[] swapDates,
      double[] swapRates,
      int numSwaps,
      double fixedSwapFreq,
      double floatSwapFreq,
      DayCount swapFixedLegDaycountFractionConvention,
      DayCount swapFloatingLegDaycountFractionConvention,
      BusinessDayConvention businessdayAdjustmentConvention,
      Calendar calendar) {

    if (numSwaps == 0) {
      return;
    }

    GenerateCreditDefaultSwapPremiumLegSchedule swapMaturities = new GenerateCreditDefaultSwapPremiumLegSchedule();

    ZonedDateTime lastStubDate = cashDates[cashDates.length - 1];

    // Need to implement this if want to have swaps with maturities less than MM instruments
    // numSwaps = getNumberOfActiveSwaps(lastStubDate, swapDates, numSwaps);

    ZonedDateTime[] unadjustedSwapDates = new ZonedDateTime[swapDates.length];
    ZonedDateTime[] adjustedSwapDates = new ZonedDateTime[swapDates.length];

    boolean[] onCycleSwapDates = new boolean[swapDates.length];
    ZonedDateTime[] previousSwapDates = new ZonedDateTime[swapDates.length];

    for (int i = 0; i < numSwaps; i++) {
      unadjustedSwapDates[i] = swapDates[i];
      adjustedSwapDates[i] = swapMaturities.businessDayAdjustDate(swapDates[i], calendar, businessdayAdjustmentConvention);
    }

    int numIntervals = 0;
    int extraDays = 0;

    int compoundInterval = 0;
    int multiplier = 0;

    double intervalYears = 0.0;

    // Need to fix this - bit of a hack
    if (swapFixedLegCouponFrequency == PeriodFrequency.SEMI_ANNUAL) {
      intervalYears = 0.5;
      multiplier = 6;
    }

    if (swapFixedLegCouponFrequency == PeriodFrequency.QUARTERLY) {
      intervalYears = 0.25;
      multiplier = 3;
    }

    // -------------------------------------------

    for (int i = 0; i < numSwaps; i++) {

      boolean onCycle = false;

      if (valueDate.getDayOfMonth() <= 28 && unadjustedSwapDates[i].getDayOfMonth() <= 28) {

        ZonedDateTime fromDate = valueDate;
        ZonedDateTime toDate = unadjustedSwapDates[i];

        double fromToYears = TimeCalculator.getTimeBetween(fromDate, toDate, ACT_365);

        int lowNumIntervals = Math.max(0, (int) Math.floor(Math.abs(fromToYears / intervalYears)) - 2);
        int index = lowNumIntervals;

        compoundInterval = index * multiplier;

        ZonedDateTime currDate = fromDate.plusMonths(compoundInterval);
        ZonedDateTime lastDate = currDate;

        while (currDate.isAfter(fromDate) && !currDate.isAfter(toDate)) {
          ++index;
          lastDate = currDate;
          currDate = valueDate.plusMonths(index * multiplier);
        }

        numIntervals = index - 1;
        extraDays = (int) Math.abs(TimeCalculator.getTimeBetween(toDate, lastDate));

        if (extraDays == 0) {
          onCycle = true;
        } // end if extraDays 

      } // end if dom <= 28

      onCycleSwapDates[i] = onCycle;

      ZonedDateTime prevDate;

      if (onCycleSwapDates[i]) {
        prevDate = valueDate.plusMonths(multiplier * (numIntervals - 1));
      }
      else {
        prevDate = unadjustedSwapDates[i].plusMonths(6 * (-1));
      }

      previousSwapDates[i] = prevDate;

    } // end loop over i

    /*
    for (int i = 0; i < numSwaps; i++) {
      System.out.println("i = " + i + "\t" + unadjustedSwapDates[i] + "\t" + adjustedSwapDates[i] + "\t" + onCycleSwapDates[i] + "\t" + previousSwapDates[i]);
    }
     */

    // -------------------------------------------

    boolean oneAlreadyAdded = false;

    boolean isEndStub = false;

    int numDates;

    for (int i = 0; i < numSwaps; i++) {

      if (adjustedSwapDates[i].isAfter(cashDates[cashDates.length - 1])) {

        if (onCycleSwapDates[i]) {
          isEndStub = true;
        }
        else {
          // Need to fill this in - jpmcdsisendstub
        } // end if

        // need to add rate = 0 case

        if (isEndStub) {

          ZonedDateTime fromDate = valueDate;
          ZonedDateTime toDate = unadjustedSwapDates[i];

          double fromToYears = TimeCalculator.getTimeBetween(fromDate, toDate, ACT_365);

          int lowNumIntervals = Math.max(0, (int) Math.floor(Math.abs(fromToYears / intervalYears)) - 2);
          int index = lowNumIntervals;

          compoundInterval = index * multiplier;

          ZonedDateTime currDate = fromDate.plusMonths(compoundInterval);
          ZonedDateTime lastDate = currDate;

          while (currDate.isAfter(fromDate) && !currDate.isAfter(toDate)) {
            ++index;
            lastDate = currDate;
            currDate = valueDate.plusMonths(index * multiplier);
          }

          numIntervals = index - 1;
          extraDays = (int) Math.abs(TimeCalculator.getTimeBetween(toDate, lastDate));

        }
        else {
          // Need to add this
        }

        if (extraDays > 0) {
          numDates = numIntervals + 2;
        }
        else {
          numDates = numIntervals + 1;
        }

        ZonedDateTime[] dateList = new ZonedDateTime[numDates];

        if (isEndStub) {

          for (int j = 0; j < numDates - 1; j++) {
            dateList[j] = valueDate.plusMonths(j * multiplier);
          }

          dateList[numDates - 1] = unadjustedSwapDates[i];

        }
        else {
          // Need to add this
        }

        for (int j = 0; j < numDates - 1; j++) {
          dateList[j] = dateList[j + 1];
        }
        numDates--;

        //

        ZonedDateTime[] adjustedDateList = new ZonedDateTime[numDates];

        for (int idx = 0; idx < numDates; idx++) {
          adjustedDateList[idx] = swapMaturities.businessDayAdjustDate(dateList[idx], calendar, businessdayAdjustmentConvention);
        }

        double[] cashflowList = new double[numDates];

        ZonedDateTime prevDate = valueDate;

        for (int idx = 0; idx < numDates; idx++) {

          ZonedDateTime cDate = adjustedDateList[idx];

          double dcf = ACT_360.getDayCountFraction(prevDate, cDate);

          cashflowList[idx] = dcf * swapRates[i];

          prevDate = cDate;
        }

        cashflowList[numDates - 1] += 1.0;

        ZonedDateTime adjMatDate = adjustedSwapDates[i];

        double price = 1.0;

        ZCAddCashFlowList(adjustedDateList, cashflowList, price, adjMatDate);

      } // end if adjdate >cashdate

    } // end loop over i

  }

  // ----------------------------------------------------------------------------------------------------------------------------------------

  void ZCAddCashFlowList(ZonedDateTime[] adjustedDateList, double[] cashflowList, double price, ZonedDateTime date) {

  }

  // ----------------------------------------------------------------------------------------------------------------------------------------

  private final double getInterpolatedRate(
      final double t,
      final double t1,
      final double t2,
      final double z1,
      final double z2) {

    final double z1t1 = z1 * t1;
    final double z2t2 = z2 * t2;

    final double zt = z1t1 + (z2t2 - z1t1) * (t - t1) / (t2 - t1);

    return zt;
  }

  // ----------------------------------------------------------------------------------------------------------------------------------------

  // ----------------------------------------------------------------------------------------------------------------------------------------

  //@Test
  public void testISDAYieldCurveBuild() {

    // Need to check the input data (numpts > 0, dates in ascending order etc)
    // None of the dates should be < baseDate

    // Need to check if baseDate is a business day

    // Need to error check for negative rates? ISDA throws an error if rate <= -1

    // JPMCDSClearExceldateSystem ??

    // The number of instruments
    int nInstr = dates.length;

    ZonedDateTime[] zCurveDates = new ZonedDateTime[nInstr];

    double[] zCurveRates = new double[nInstr];

    double[] zCurveCCRates = new double[nInstr];

    // The set of input instrument dates and derived discount factors
    double[] zCurveDiscountFactors = new double[nInstr];

    ZonedDateTime valueDate = baseDate;

    DayCount mmDCC = moneyMarketDaycountFractionConvention;

    // TODO : Calculate these automatically

    // Fixed swap interval is 6M (2 payments per year)
    double fixedSwapFreq = 2.0;

    // Float swap interval is 3M (4 payments per year)
    double floatSwapFreq = 4.0;

    // ------------------------------------------------------------------------------------------------

    int nSwap = getNumberOfInstruments(rateTypes, ISDAInstrumentTypes.Swap);
    int nCash = getNumberOfInstruments(rateTypes, ISDAInstrumentTypes.MoneyMarket);

    ZonedDateTime[] swapDates = getInstrumentDates(dates, rateTypes, ISDAInstrumentTypes.Swap, nSwap);
    ZonedDateTime[] cashDates = getInstrumentDates(dates, rateTypes, ISDAInstrumentTypes.MoneyMarket, nCash);

    double[] swapRates = getInstrumentRates(rates, rateTypes, ISDAInstrumentTypes.Swap, nSwap);
    double[] cashRates = getInstrumentRates(rates, rateTypes, ISDAInstrumentTypes.MoneyMarket, nCash);

    // ------------------------------------------------------------------------------------------------

    // TODO : Check that nCash != 0

    final double basis = 1.0;

    for (int i = 0; i < nCash; i++) {

      // TODO : Need to check the stubs as per the ISDA model

      double dcf = TimeCalculator.getTimeBetween(baseDate, cashDates[i], mmDCC);

      double denom = 1.0 + dcf * cashRates[i];

      double discount = 1.0 / denom;

      double dcf2 = TimeCalculator.getTimeBetween(baseDate, cashDates[i], ACT_365);
      double rate = Math.pow(discount, -1.0 / (basis * dcf2)) - 1.0;

      zCurveDates[i] = cashDates[i];
      zCurveRates[i] = cashRates[i];
      zCurveCCRates[i] = rate;
      zCurveDiscountFactors[i] = discount;

      //System.out.println("i = " + i + "\t" + baseDate + "\t" + cashDates[i] + "\t" + cashRates[i] + "\t" + dcf + "\t" + discount);
    }

    // ----------------------------------------------------------------------------------------------

    // Interpolation/extrapolation part

    for (long i = 1; i < 1500; i++)
    {

      ZonedDateTime testDate = zdt(2012, 11, 14, 0, 0, 0, 0, ZoneOffset.UTC).plusDays(i);

      ZonedDateTime loDate;
      ZonedDateTime hiDate;

      double loRate = 0.0;
      double hiRate = 0.0;

      double z1 = 0.0;
      double z2 = 0.0;

      double t1 = 0.0;
      double t2 = 0.0;
      double t = 0.0;
      double Z = 0.0;
      double rate = 0.0;

      // Extrapolation below the first date
      if (testDate.isBefore(zCurveDates[0])) {
        loRate = zCurveCCRates[0];
        z1 = Math.log(1.0 + loRate);
        t = TimeCalculator.getTimeBetween(valueDate, testDate, ACT_365);
        rate = z1;
        Z = Math.exp(-rate * t);
      }

      // Extrapolation beyond the last data
      if (!testDate.isBefore(zCurveDates[nInstr - 1])) {

        int lo = nInstr - 2;
        int hi = nInstr - 1;

        t1 = TimeCalculator.getTimeBetween(valueDate, zCurveDates[lo], ACT_360);
        t2 = TimeCalculator.getTimeBetween(valueDate, zCurveDates[hi], ACT_360);
        t = TimeCalculator.getTimeBetween(valueDate, testDate, ACT_360);

        loRate = zCurveCCRates[lo];
        hiRate = zCurveCCRates[hi];

        z1 = Math.log(1.0 + loRate);
        z2 = Math.log(1.0 + hiRate);

        // TODO : DoubleToBits this
        if (t == 0.0)
        {

          // TODO : Check for t2 == 0 as well
          t = 1.0 / 365.0;
        }

        final double zt = getInterpolatedRate(t, t1, t2, z1, z2);
        rate = zt / t;

        t = TimeCalculator.getTimeBetween(valueDate, testDate, ACT_365);

        Z = Math.exp(-rate * t);
      }

      // Interpolation
      if (!testDate.isBefore(zCurveDates[0]) && testDate.isBefore(zCurveDates[nInstr - 1]))
      {
        // ... testDate is within the window spanned by the input dates

        int lo = 0;

        // Start at the first date
        ZonedDateTime rollingDate = zCurveDates[0];

        while (!rollingDate.isAfter(testDate))
        {
          lo++;
          rollingDate = zCurveDates[lo];
        }

        int hi = lo + 1;

        loDate = zCurveDates[lo - 1];
        hiDate = zCurveDates[hi - 1];

        loRate = zCurveCCRates[lo - 1];
        hiRate = zCurveCCRates[hi - 1];

        z1 = Math.log(1.0 + loRate);
        z2 = Math.log(1.0 + hiRate);

        t1 = TimeCalculator.getTimeBetween(valueDate, loDate, ACT_360);
        t2 = TimeCalculator.getTimeBetween(valueDate, hiDate, ACT_360);
        t = TimeCalculator.getTimeBetween(valueDate, testDate, ACT_360);

        // TODO : DoubleToBits this
        if (t == 0.0)
        {

          // TODO : Check for t2 == 0 as well
          t = 1.0 / 365.0;
        }

        final double zt = getInterpolatedRate(t, t1, t2, z1, z2);
        rate = zt / t;

        t = TimeCalculator.getTimeBetween(valueDate, testDate, ACT_365);

        Z = Math.exp(-rate * t);
      }

      //System.out.println(testDate + "\t" + Z);
    }

    // ------------------------------------------------------------------------------------------------------------

    /*
    jpmCDSZCSwaps(
        valueDate,
        cashDates,
        swapDates,
        swapRates,
        nSwap,
        fixedSwapFreq,
        floatSwapFreq,
        swapFixedLegDaycountFractionConvention,
        swapFloatingLegDaycountFractionConvention,
        businessdayAdjustmentConvention,
        calendar);
     */

    // ------------------------------------------------------------------------------------------------

    int numSwaps = getNumberOfActiveSwaps(cashDates[cashDates.length - 1], swapDates, nSwap);

    // Check that nSwaps > 0

    GenerateCreditDefaultSwapPremiumLegSchedule swapMaturities = new GenerateCreditDefaultSwapPremiumLegSchedule();

    if (numSwaps > 0) {

      boolean useFastZC = false;

      ZonedDateTime prevDate;

      // ---------------------------------------------------

      // ZCAddSwaps

      // Now calc the swap rates and discount factors

      /*
      boolean oneAlreadyAdded = false;

      double price = 1.0;

      for (int i = 0; i < swapDates.length; i++) {

        if (adjustedSwapDates[i].isAfter(cashDates[cashDates.length - 1])) {

          boolean isEndStub = false;

          // Fixed leg swap cashflow freq in months
          double ivl = 6.0;

          if (onCycleSwapDates[i]) {
            isEndStub = true;
          }
          else {
            // Need to fill this in
          }

          // ZCGetSwapCFL
          if (swapRates[i] == 0.0) {
            // Need to fill this in
          }

          int numDates;

          // ZCGetSwapCouponDL -> NewPayDates -> NewDateList
          if (isEndStub) {
            // CountDates

            ZonedDateTime fromDate = valueDate;
            ZonedDateTime toDate = unadjustedSwapDates[i];

            double fromToYears = TimeCalculator.getTimeBetween(fromDate, toDate, ACT_365);

            int lowNumIntervals = Math.max(0, (int) Math.floor(Math.abs(fromToYears / intervalYears)) - 2);
            int index = lowNumIntervals;

            // Need to fix this - bit of a hack
            if (swapFixedLegCouponFrequency == PeriodFrequency.SEMI_ANNUAL) {
              multiplier = 6;
              compoundInterval = index * multiplier;
            }

            if (swapFixedLegCouponFrequency == PeriodFrequency.QUARTERLY) {
              multiplier = 3;
              compoundInterval = index * multiplier;
            }

            ZonedDateTime currDate = valueDate.plusMonths(compoundInterval);
            ZonedDateTime lastDate = currDate;

            while (currDate.isAfter(fromDate) && !currDate.isAfter(toDate)) {
              ++index;
              lastDate = currDate;
              currDate = valueDate.plusMonths(index * multiplier);
            }

            numIntervals = index - 1;
            extraDays = (int) Math.abs(TimeCalculator.getTimeBetween(toDate, lastDate));

          }
          else {
            // Need to fill this in
          }

          if (extraDays > 0) {
            numDates = numIntervals + 2;
          }
          else {
            numDates = numIntervals + 1;
          }

          ZonedDateTime[] dateList = new ZonedDateTime[numDates];

          if (isEndStub) {
            // MakeTDateArray

            for (int idx = 0; idx < numDates - 1; idx++) {

              double offsetInterval = idx * 0.5;

              // DtFwdAny
              dateList[idx] = valueDate.plusMonths(idx * 6);

              System.out.println(dateList[idx]);

            }

            dateList[numDates - 1] = unadjustedSwapDates[i];
          }
          else {
            // Need to fill this in
          }

          for (int idx = 0; idx < numDates - 1; idx++) {
            dateList[idx] = dateList[idx + 1];
          }

          numDates--;

          ZonedDateTime[] adjustedDateList = new ZonedDateTime[numDates];

          for (int idx = 0; idx < numDates; idx++) {
            adjustedDateList[idx] = swapMaturities.businessDayAdjustDate(dateList[idx], calendar, businessdayAdjustmentConvention);
          }

          double[] cashflowList = new double[numDates];

          prevDate = valueDate;

          for (int idx = 0; idx < numDates; idx++) {

            double dcf = TimeCalculator.getTimeBetween(prevDate, adjustedDateList[idx], ACT_365);

            cashflowList[idx] = swapRates[i] * dcf;

            prevDate = adjustedDateList[idx];

            //System.out.println(cashflowList[idx]);
          }

        }

        cashflowList[numDates - 1] += 1.0;

      } // end loop over i
       */

    } // end if nSwaps > 0

    // ----------------------------------------------------------------------------------------------------------------------------------------
  }

  //-------------------------------------------------------------------------
  private static ZonedDateTime zdt(int y, int m, int d, int hr, int min, int sec, int nanos, ZoneOffset offset) {
    return LocalDateTime.of(y, m, d, hr, min, sec, nanos).atZone(offset);
  }

}