/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.credit.creditdefaultswap.pricing.vanilla.isdanew;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Period;
import org.threeten.bp.temporal.JulianFields;

import com.opengamma.analytics.financial.credit.StubType;
import com.opengamma.financial.convention.businessday.BusinessDayConvention;
import com.opengamma.financial.convention.calendar.Calendar;
import com.opengamma.util.ArgumentChecker;

/**
 * For a CDS with set set of payments on the fixed leg, this holds the payments dates and the accrual start and end dates. It does not
 * hold the payment amounts with depends on the day-count (normally ACT/360) and the spread.
 */
public class ISDAPremiumLegSchedule {

  private final int _nPayments;
  private final LocalDate[] _accStartDates;
  private final LocalDate[] _accEndDates;
  private final LocalDate[] _paymentDates;

  public static ISDAPremiumLegSchedule truncateSchedule(final LocalDate stepin, final ISDAPremiumLegSchedule schedule) {
    if (!schedule._accEndDates[0].isBefore(stepin)) {
      return schedule; // nothing to truncate
    }
    if (stepin.isAfter(schedule._accEndDates[schedule._nPayments - 1])) {
      throw new IllegalArgumentException("today is after last payment - i.e. all the payments are in the past");
    }

    int index = 0;
    while (schedule._accEndDates[index].isBefore(stepin)) {
      index++;
    }

    return new ISDAPremiumLegSchedule(schedule._paymentDates, schedule._accStartDates, schedule._accEndDates, index);
  }

  /**
   * Truncation constructor
   * @param paymentDates
   * @param accStartDates
   * @param accEndDates
   * @param index copy the date starting from this index
   */
  private ISDAPremiumLegSchedule(final LocalDate[] paymentDates, final LocalDate[] accStartDates, final LocalDate[] accEndDates, final int index) {
    ArgumentChecker.noNulls(paymentDates, "null paymentDates");
    ArgumentChecker.noNulls(accStartDates, "accStartDates");
    ArgumentChecker.noNulls(accEndDates, "null accEndDates");

    final int n = paymentDates.length;
    _nPayments = n - index;
    ArgumentChecker.isTrue(n == accStartDates.length, "accStartDates length of {} does not match paymentDates length of {}", accStartDates.length, _nPayments);
    ArgumentChecker.isTrue(n == accEndDates.length, "accEndDates length of {} does not match paymentDates length of {}", accEndDates.length, _nPayments);

    _paymentDates = new LocalDate[_nPayments];
    _accStartDates = new LocalDate[_nPayments];
    _accEndDates = new LocalDate[_nPayments];
    System.arraycopy(paymentDates, index, _paymentDates, 0, _nPayments);
    System.arraycopy(accStartDates, index, _accStartDates, 0, _nPayments);
    System.arraycopy(accEndDates, index, _accEndDates, 0, _nPayments);
  }

  /**
   * Mimics JpmcdsCdsFeeLegMake
   * @param startDate The protection start date
   * @param endDate The protection end date
   * @param step The period or frequency at which payments are made (e.g. every three months)
   * @param stubType Options are FRONTSHORT, FRONTLONG, BACKSHORT, BACKLONG or NONE - <b>Note</b> in this code NONE is not allowed
   * @param businessdayAdjustmentConvention options are 'following' or 'proceeding'
   * @param calandar A holiday calendar
   * @param protectionStart If true, protection starts are the beginning rather than end of day (protection still ends at end of day).
   */
  public ISDAPremiumLegSchedule(final LocalDate startDate, final LocalDate endDate, final Period step, final StubType stubType, final BusinessDayConvention businessdayAdjustmentConvention,
      final Calendar calandar, final boolean protectionStart) {
    ArgumentChecker.notNull(startDate, "null startDate");
    ArgumentChecker.notNull(endDate, "null endDate");
    ArgumentChecker.notNull(stubType, "null stubType");

    if (protectionStart) {
      ArgumentChecker.isFalse(endDate.isBefore(startDate), "end date is before startDate");
    } else {
      ArgumentChecker.isTrue(endDate.isAfter(startDate), "end is must be after startDate");
    }

    LocalDate[] tempDates;
    if (startDate.isEqual(endDate)) { // this can only happen if protectionStart == true
      tempDates = new LocalDate[2];
      tempDates[0] = startDate;
      tempDates[1] = endDate;
    } else {
      tempDates = getUnadjustedDates(startDate, endDate, step, stubType);
    }

    _nPayments = tempDates.length - 1;
    _paymentDates = new LocalDate[_nPayments];
    _accStartDates = new LocalDate[_nPayments];
    _accEndDates = new LocalDate[_nPayments];

    LocalDate dPrev = tempDates[0];
    LocalDate dPrevAdj = dPrev; // first date is never adjusted
    for (int i = 0; i < _nPayments; i++) {
      final LocalDate dNext = tempDates[i + 1];
      final LocalDate dNextAdj = businessDayAdjustDate(dNext, calandar, businessdayAdjustmentConvention);
      _accStartDates[i] = dPrevAdj;
      _accEndDates[i] = dNextAdj;
      _paymentDates[i] = dNextAdj;
      dPrev = dNext;
      dPrevAdj = dNextAdj;
    }

    // the last accrual date is not adjusted.
    if (protectionStart) {
      _accEndDates[_nPayments - 1] = dPrev.plusDays(1); // extra day of accrued interest
    } else {
      _accEndDates[_nPayments - 1] = dPrev;
    }
  }

  public int getNumPayments() {
    return _nPayments;
  }

  public LocalDate getAccStartDate(final int index) {
    return _accStartDates[index];
  }

  public LocalDate getAccEndDate(final int index) {
    return _accEndDates[index];
  }

  public LocalDate getPaymentDate(final int index) {
    return _paymentDates[index];
  }

  /**
   * finds the index in accStartDate that matches the given date, or if date is not a member of accStartDate returns (-insertionPoint -1)
   * @see Arrays#binarySearch
   * @param date The date to find
   * @return index or code giving insertion point
   */
  public int getAccStartDateIndex(final LocalDate date) {
    return Arrays.binarySearch(_accStartDates, date, null);
  }

  /**
   * The accrual start date, end date and payment date at the given index
   * @param index the index (from zero)
   * @return array of LocalDate
   */
  public LocalDate[] getAccPaymentDateTriplet(final int index) {
    return new LocalDate[] {_accStartDates[index], _accEndDates[index], _paymentDates[index]};
  }

  /**
   * This mimics JpmcdsDateListMakeRegular. Produces a set of ascending dates by following the rules:<p>
   * If the stub is at the front end, we role backwards from the endDate at an integer multiple of the specified step size (e.g. 3M),
   * adding these date until we pass the startDate(this date is not added). If the stub type is short, the startDate is added (as the first date), hence the first period
   * will be less than (or equal to) the remaining periods. If the stub type is long, the startDate is also added, but the date immediately
   * after that is removed, so the first period is longer than the remaining.<p>
   * If the stub is at the back end, we role forward from the startDate at an integer multiple of the specified step size (e.g. 3M),
   * adding these date until we pass the endDate(this date is not added). If the stub type is short, the endDate is added (as the last date), hence the last period
   * will be less than (or equal to) the other periods. If the stub type is long, the endDate is also added, but the date immediately
   * before that is removed, so the last period is longer than the others.
   *
   * @param startDate The start date - this will be the first entry in the list
   * @param endDate The end date - this will be the last entry in the list
   * @param step the step period (e.g. 3M - will produce dates every 3 months, with adjustments at the beginning or end based on stub type)
   * @param stubType Options are FRONTSHORT, FRONTLONG, BACKSHORT, BACKLONG or NONE - <b>Note</b> in this code NONE is not allowed
   * @return an array of LocalDate
   */
  private LocalDate[] getUnadjustedDates(final LocalDate startDate, final LocalDate endDate, final Period step, final StubType stubType) {
    // TODO remove NONE fromStubType enumeration
    ArgumentChecker.isFalse(stubType == StubType.NONE, "NONE is not allowed as a stubType");

    final long firstJulianDate = startDate.getLong(JulianFields.MODIFIED_JULIAN_DAY);
    final long secondJulianDate = endDate.getLong(JulianFields.MODIFIED_JULIAN_DAY);
    final double days = step.getDays() + 365.0 * (step.getMonths() / 12. + step.getYears());
    final int nApprox = 3 + (int) ((secondJulianDate - firstJulianDate) / days);

    final List<LocalDate> dates = new ArrayList<>(nApprox);

    // stub at front end, so start at endDate and work backwards
    if (stubType == StubType.FRONTSHORT || stubType == StubType.FRONTLONG) {

      int intervals = 0;
      LocalDate tDate = endDate;
      while (tDate.isAfter(startDate)) {
        dates.add(tDate);
        final Period tStep = step.multipliedBy(++intervals); // this mimics ISDA c code, rather than true market convention
        tDate = endDate.minus(tStep);
      }

      final int n = dates.size();
      if (tDate.isEqual(startDate) || n == 1 || stubType == StubType.FRONTSHORT) {
        dates.add(startDate);
      } else {
        // long front stub - remove the last date entry in the list and replace it with startDate
        dates.remove(n - 1);
        dates.add(startDate);
      }

      final int m = dates.size();
      final LocalDate[] res = new LocalDate[m];
      // want to output in ascending chronological order, so need to reverse the list
      int j = m - 1;
      for (int i = 0; i < m; i++, j--) {
        res[j] = dates.get(i);
      }
      return res;

      // stub at back end, so start at startDate and work forward
    } else {
      int intervals = 0;
      LocalDate tDate = startDate;
      while (tDate.isBefore(endDate)) {
        dates.add(tDate);
        final Period tStep = step.multipliedBy(++intervals); // this mimics ISDA c code, rather than true market convention
        tDate = startDate.plus(tStep);
      }

      final int n = dates.size();
      if (tDate.isEqual(endDate) || n == 1 || stubType == StubType.BACKSHORT) {
        dates.add(endDate);
      } else {
        // long back stub - remove the last date entry in the list and replace it with endDate
        dates.remove(n - 1);
        dates.add(endDate);
      }
      final LocalDate[] res = new LocalDate[dates.size()];
      return dates.toArray(res);
    }
  }

  private LocalDate businessDayAdjustDate(final LocalDate date, final Calendar calendar, final BusinessDayConvention convention) {

    ArgumentChecker.notNull(date, "date");
    ArgumentChecker.notNull(calendar, "Calendar");
    ArgumentChecker.notNull(convention, "Business day adjustment");

    return convention.adjustDate(calendar, date);
  }

}
