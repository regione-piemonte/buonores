/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { Injectable } from '@angular/core';
import { AppToastService } from '@buonores-shared/toast/app-toast.service';
import { BuonoresError } from './buonores-error.model';

@Injectable()
export class BuonoresErrorService {

    constructor(private toastService: AppToastService) {}

    handleError(error: BuonoresError) {
        this.toastService.showServerError(error);
    }

    handleWarning(warn: BuonoresError) {
        this.toastService.showWarning(warn);
    }
}
