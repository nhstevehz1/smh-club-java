import {AuthService} from "../services/auth.service";

export function authAppInitFactory(authService: AuthService): () => Promise<void> {
    return (): Promise<void> => authService.startupLoginSequence().then(() => {});
}
