export * from './actuator-api';
import {ActuatorApi} from './actuator-api';
import {AppConfigApi} from './app-config-api';
import {VerifierApi} from './verifier-api';

export * from './app-config-api';
export * from './verifier-api';
export const APIS = [ActuatorApi, AppConfigApi, VerifierApi];
