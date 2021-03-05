package com.example.slickkwear;

public class SpinnerItems {
    public String spinnerItemName;
    public String spinnerItemID;

//    public SpinnerItems(String namePart, Object idPart) {
//        spinnerItemName = namePart;
//        spinnerItemID = idPart;
//    }


    public SpinnerItems(String spinnerItemName, String spinnerItemID) {
        this.spinnerItemName = spinnerItemName;
        this.spinnerItemID = spinnerItemID;
    }

    @Override
    public String toString() {
        return spinnerItemName;
    }

}