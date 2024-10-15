/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';


@Component({
  selector: 'app-custom-dialog',
  templateUrl: './custom-dialog.component.html',
  styleUrls: ['./custom-dialog.component.css']
})
export class CustomDialogComponent implements OnInit {
  
  attenzione: string = '';

  constructor(public dialogRef: MatDialogRef<CustomDialogComponent>,
      @Inject(MAT_DIALOG_DATA) public data: {titolo:string, messaggio:string}) {}

  ngOnInit() {
  }

  conferma(){
		this.dialogRef.close(true);
	}

  annulla(){
		this.dialogRef.close(false);
	}

}