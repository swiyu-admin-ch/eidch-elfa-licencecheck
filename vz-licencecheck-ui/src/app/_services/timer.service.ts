import {Injectable} from '@angular/core';
import {Observable, Subscription} from 'rxjs';
import {environment} from '@environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TimerService {
  private timerSubscription: Subscription;
  private timerObservable: Observable<0>;

  startTimeoutTimer(): Observable<0> {
    // Create a new observable that uses setTimeout internally
    this.timerObservable = new Observable<0>(observer => {
      const handle = setTimeout(() => {
        observer.next(0);
        observer.complete();
      }, environment.pollingTimeoutTime);

      // Return teardown logic that clears the timeout when unsubscribed
      return () => clearTimeout(handle);
    });

    return this.timerObservable;
  }

  stopTimer() {
    this.timerSubscription?.unsubscribe();
    if (this.timerObservable) {
      this.timerObservable = null;
    }
  }

  subscribeToTimer(f: {next: () => void}) {
    this.timerSubscription = this.timerObservable.subscribe(f);
  }
}
