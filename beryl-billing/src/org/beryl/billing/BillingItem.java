package org.beryl.billing;

import android.os.Parcel;
import android.os.Parcelable;

class BillingItem implements Parcelable {
	public final String productId;
	public final String developerPayload;
	
	public BillingItem(final String productId) {
		this(productId, null);
	}
	
	public BillingItem(final String productId, final String developerPayload) {
		this.productId = productId;
		this.developerPayload = developerPayload;
	}
	
	private BillingItem(final Parcel in) {
        productId = in.readString();
        developerPayload = in.readString();
    }
	
	public int describeContents() {
        return 0;
    }

    public void writeToParcel(final Parcel out, final int flags) {
        out.writeString(productId);
        out.writeString(developerPayload);
    }

    public static final Parcelable.Creator<BillingItem> CREATOR
            = new Parcelable.Creator<BillingItem>() {
        public BillingItem createFromParcel(final Parcel in) {
            return new BillingItem(in);
        }

        public BillingItem[] newArray(final int size) {
            return new BillingItem[size];
        }
    };
    
	@Override
	public String toString() {
		return "BillingItemModel {productId= " + productId + ", developerPayload= " + developerPayload + "}";
	}
}
