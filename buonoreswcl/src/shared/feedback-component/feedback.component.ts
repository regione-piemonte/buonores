/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";

@Component({
    selector: 'app-feedback',
    templateUrl: './feedback.component.html',
    styleUrls: ['./feedback.component.css']
  })
  export class FeedbackComponent implements OnInit {

    @Input() tipoFeedback: string;
    @Input() messaggio: string;
    @Output() close = new EventEmitter<null>();

    ngOnInit() {
        // this.messaggio = 'MESSAGGIO prova test'
        // this.tipoFeedback = 'success'
    }

    onCloseFeedback() {
        this.close.emit();
    }
  }