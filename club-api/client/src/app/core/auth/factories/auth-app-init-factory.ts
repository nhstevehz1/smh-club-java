import {AuthService} from "../services/auth.service";

export function authAppInitFactory(authService: AuthService): () => Promise<void> {
    return () => authService.startupLoginSequence().then(() => {});
}
