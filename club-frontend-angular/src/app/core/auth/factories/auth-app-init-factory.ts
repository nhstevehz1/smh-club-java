import {AuthService} from "../services/auth.service";

export function authAppInitFactory(authService: AuthService): () => Promise<void> {
    console.log('running auth init factor');
    return () => authService.startupLoginSequence().then(() => console.log('startup then'));
}
