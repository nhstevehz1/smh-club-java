import {HttpClient} from "@angular/common/http";
import {TranslateLoader, TranslateModule} from "@ngx-translate/core";
import {TranslateHttpLoader} from "@ngx-translate/http-loader";
import {EnvironmentProviders, importProvidersFrom} from "@angular/core";

const httpLoaderFactory: (http: HttpClient) => TranslateLoader = (http: HttpClient) =>
    new TranslateHttpLoader(http, './i18n/', '.json');

export function provideNgxTranslate(): EnvironmentProviders {
    return importProvidersFrom(TranslateModule.forRoot({
        loader: {
            provide: TranslateLoader,
            useFactory: httpLoaderFactory,
            deps: [HttpClient]
        }
    }));
}
