import {AuthService} from '@app/core/auth/services/auth.service';

export function authAppInitFactory(authService: AuthService): () => Promise<void> {
    return (): Promise<void> => authService.startupLoginSequence().then();
}
