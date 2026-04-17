import {Injectable} from '@angular/core';
import {checkImage} from 'check-image-type';

@Injectable({
  providedIn: 'root'
})
export class QrCodeUtil {
  static readonly SAFE_IMAGE_FORMATS = ['png', 'jpg', 'jpeg'];

  public static sanitize(data: string, format: string): string {
    // format
    let isFormat = QrCodeUtil.SAFE_IMAGE_FORMATS.includes(format);

    // content
    let buffer = this.getBuffer(data);
    const imageType = checkImage(buffer);
    const isContent = !!(imageType && imageType.ext === format);

    if (isFormat && isContent) {
      return data;
    } else {
      // todo: log security violations ?
      return null;
    }
  }
  private static getBuffer(data: string) {
    let binaryString = window.atob(data);
    let buffer = new Uint8Array(binaryString.length);
    for (let i = 0; i < binaryString.length; i++) {
      buffer[i] = binaryString.charCodeAt(i);
    }
    return buffer;
  }
}
