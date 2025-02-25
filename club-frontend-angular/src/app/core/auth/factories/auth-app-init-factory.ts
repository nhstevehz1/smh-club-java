import {AuthService} from "../services/auth.service";

export function authAppInitFactory(authService: AuthService): () => Promise<void> {
    console.debug('running auth init factory');
    return () => authService.startupLoginSequence().then(() => console.debug('startup then'));
}
