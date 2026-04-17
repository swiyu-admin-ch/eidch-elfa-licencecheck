import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DateUtils {
  static parseDate(dateString: string): Date {
    if (!dateString) return null;

    const s = dateString.trim();

    // YYYY-MM-DD (optionally with time after the day, e.g. 2025-08-13T12:00:00)
    const iso = /^(\d{4})-(\d{2})-(\d{2})(?:[T\s].*)?$/;
    const m1 = s.match(iso);
    if (m1) {
      const [, y, mo, d] = m1;
      return new Date(Number(y), Number(mo) - 1, Number(d));
    }

    // DD.MM.YYYY
    const dot = /^(\d{2})\.(\d{2})\.(\d{4})$/;
    const m2 = s.match(dot);
    if (m2) {
      const [, d, mo, y] = m2;
      return new Date(Number(y), Number(mo) - 1, Number(d));
    }

    return null;
  }
}
