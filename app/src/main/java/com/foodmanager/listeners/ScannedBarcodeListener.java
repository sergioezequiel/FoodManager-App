package com.foodmanager.listeners;

import com.foodmanager.models.CodigoBarras;

public interface ScannedBarcodeListener {
    void openAddDialog(CodigoBarras barcode);
}
