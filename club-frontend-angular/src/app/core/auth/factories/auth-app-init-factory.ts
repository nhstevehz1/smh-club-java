import {AuthService} from "../services/auth.service";
import {firstValueFrom} from "rxjs";

export function authAppInitFactory(authService: AuthService): () => Promise<void> {
    console.log('running auth init factor');
    return () => authService.startupLoginSequence().then(() => console.log('startup then'));
}

export function appInitTest(): () => Promise<void> {
    myTest();
    return () => Promise.resolve();

}

export function myTest(): void {
    console.log('Im in myTest');
}
