/*
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package billingmodule.billing;

import com.android.billingclient.api.BillingClient.SkuType;

import java.util.Arrays;
import java.util.List;

/**
 * Static fields and methods useful for billing
 */
public final class BillingConstants {
    // SKUs for our products: the premium upgrade (non-consumable) and gas (consumable)
    public static final String SKU_PERMANENT = "purchase_permanent";
    //public static final String SKU_GAS = "";

    // SKU for our subscription (infinite gas)
    //public static final String SKU_GOLD_MONTHLY = "";
    public static final String SKU_YEARLY = "subscription_year";

    private static final String[] IN_APP_SKUS = {SKU_PERMANENT};
    private static final String[] SUBSCRIPTIONS_SKUS = {SKU_YEARLY};

    private BillingConstants() {
    }

    /**
     * Returns the list of all SKUs for the billing type specified
     */
    public static final List<String> getSkuList(@SkuType String billingType) {
        return (billingType == SkuType.INAPP) ? Arrays.asList(IN_APP_SKUS)
                : Arrays.asList(SUBSCRIPTIONS_SKUS);
    }
}

