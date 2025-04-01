import {AuthService} from '@app/core/auth';

export function authAppInitFactory(authService: AuthService): () => Promise<void> {
    return (): Promise<void> => authService.startupLoginSequence().then();
}
