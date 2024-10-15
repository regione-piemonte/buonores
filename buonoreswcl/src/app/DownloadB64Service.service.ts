/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DownloadB64ServiceService {

constructor() { }


private b64toBlob(b64Data, contentType) {
  contentType = contentType || '';
  let sliceSize = 512;

  var byteCharacters = atob(b64Data);
  var byteArrays = [];

  for (var offset = 0; offset < byteCharacters.length; offset += sliceSize) {
      var slice = byteCharacters.slice(offset, offset + sliceSize);

      var byteNumbers = new Array(slice.length);
      for (var i = 0; i < slice.length; i++) {
          byteNumbers[i] = slice.charCodeAt(i);
      }

      var byteArray = new Uint8Array(byteNumbers);

      byteArrays.push(byteArray);
  }

  var blob = new Blob(byteArrays, { type: contentType });
  return blob;
}
  getFile(base64data:string,mimeType:string,fileName:string) {
    var blob = this.b64toBlob(base64data, mimeType);
    let a = document.createElement("a");
    document.body.appendChild(a);
    var url = window.URL.createObjectURL(blob);
    a.href = url;
    a.download = String(fileName);
    a.click();
    window.URL.revokeObjectURL(url);
    a.remove();
  }
}
