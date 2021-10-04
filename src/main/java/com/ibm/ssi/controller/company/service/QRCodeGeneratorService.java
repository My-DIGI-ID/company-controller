/*
 * Copyright 2021 Bundesrepublik Deutschland
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibm.ssi.controller.company.service;

import java.awt.image.BufferedImage;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.ibm.ssi.controller.company.service.exceptions.QRCodeGenerationException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class QRCodeGeneratorService {
  @Value("${qr.size: 300}")
  private Integer size;
  private QRCodeWriter qrCodeWriter = new QRCodeWriter();
  public BufferedImage generateQRCodeImage(String text) throws QRCodeGenerationException {
    try {
      BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, this.size, this.size);
      return MatrixToImageWriter.toBufferedImage(bitMatrix);
    } catch (WriterException e) {
      throw new QRCodeGenerationException(e);
    }
  }
}
