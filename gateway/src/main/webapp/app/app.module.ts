import './vendor.ts';
import { AuthInterceptor } from './blocks/interceptor/auth.interceptor';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Ng2Webstorage } from 'ng2-webstorage';

import { FoodAppetencySharedModule, UserRouteAccessService } from './shared';
import { FoodAppetencyHomeModule } from './home/home.module';
import { FoodAppetencyAdminModule } from './admin/admin.module';
import { FoodAppetencyAccountModule } from './account/account.module';
import { FoodAppetencyEntityModule } from './entities/entity.module';

import { customHttpProvider } from './blocks/interceptor/http.provider';
import { PaginationConfig } from './blocks/config/uib-pagination.config';

import {
    JhiMainComponent,
    LayoutRoutingModule,
    NavbarComponent,
    FooterComponent,
    ProfileService,
    PageRibbonComponent,
    ActiveMenuDirective,
    ErrorComponent
} from './layouts';

@NgModule({
    imports: [
        BrowserModule,
        LayoutRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-'}),
        FoodAppetencySharedModule,
        FoodAppetencyHomeModule,
        FoodAppetencyAdminModule,
        FoodAppetencyAccountModule,
        FoodAppetencyEntityModule
    ],
    declarations: [
        JhiMainComponent,
        NavbarComponent,
        ErrorComponent,
        PageRibbonComponent,
        ActiveMenuDirective,
        FooterComponent
    ],
    providers: [
        ProfileService,
        customHttpProvider(),
        PaginationConfig,
        UserRouteAccessService
    ],
    bootstrap: [ JhiMainComponent ]
})
export class FoodAppetencyAppModule {}
