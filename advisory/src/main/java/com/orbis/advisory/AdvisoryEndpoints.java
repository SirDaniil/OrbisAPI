package com.orbis.advisory;

import com.github.sd.*;
import org.json.*;

/**
 * User: Daniil Sosonkin
 * Date: 12/7/2018 4:06 PM
 */
public class AdvisoryEndpoints
    {
        private AdvisoryEndpoints()
            { }

        public static final Endpoint BranchPortfolio = () -> "/v1/branch/portfolio";
        public static final Endpoint BranchRtb = () -> "/v1/branch/rtb";
        public static final Endpoint BranchRtbsTotal = () -> "/v1/branch/rtbs/total";
        public static final Endpoint BranchRtbs = new Endpoint()
            {
                @Override
                public String getPath()
                    {
                        return "/v1/branch/rtbs";
                    }

                @Override
                public Class getDatatype()
                    {
                        return JSONArray.class;
                    }
            };

        public static final Endpoint BranchInventories = new Endpoint()
            {
                @Override
                public String getPath()
                    {
                        return "/v1/branch/inventory";
                    }

                @Override
                public Class getDatatype()
                    {
                        return JSONArray.class;
                    }
            };

        public static final Endpoint PositionSearch = new Endpoint()
            {
                @Override
                public String getPath()
                    {
                        return "/user/position/search";
                    }

                @Override
                public Class getDatatype()
                    {
                        return JSONArray.class;
                    }
            };

        public static final Endpoint AgreementsUnisigned = new Endpoint()
            {
                @Override
                public String getPath()
                    {
                        return "/user/agreements/unsigned";
                    }

                @Override
                public Class getDatatype()
                    {
                        return JSONArray.class;
                    }
            };

        public static final Endpoint PasswordChange = () -> "/auth/v1/password/change";

        public static final Endpoint DeviceRegistration = () -> "/user/device/register";

        public static final Endpoint UserInfo = () -> "/user/info";

        public static final Endpoint AdvisoryUsers = new Endpoint() {
            @Override
            public String getPath()
                {
                    return "/v2/advisory/clients/list";
                }

            @Override
            public Class getDatatype()
                {
                    return JSONArray.class;
                }
        };

        public static final Endpoint AdvisoryUserAccounts = new Endpoint() {
            @Override
            public String getPath()
                {
                    return "/v2/advisory/clients/accounts";
                }

            @Override
            public Class getDatatype()
                {
                    return JSONArray.class;
                }
        };

        public static final Endpoint AdvisoryUserNotes = new Endpoint() {
            @Override
            public String getPath()
                {
                    return "/v2/advisory/clients/user/notes";
                }

            @Override
            public Class getDatatype()
                {
                    return JSONArray.class;
                }
        };

        public static final Endpoint AdvisoryUserNotesAdd = () -> "/v2/advisory/clients/user/notes/add";

        public static final Endpoint AdvisoryAccountStats = () -> "/v2/advisory/clients/accounts/stats";

        public static final Endpoint AdvisoryAccountNotes = new Endpoint() {
            @Override
            public String getPath()
                {
                    return "/v2/advisory/clients/account/notes";
                }

            @Override
            public Class getDatatype()
                {
                    return JSONArray.class;
                }
        };

        public static final Endpoint AdvisoryAccountNotesAdd = () -> "/v2/advisory/clients/account/notes/add";

        public static final Endpoint AdvisoryModelUpdateComponent = () -> "/v2/advisory/model/component/update";

        public static final Endpoint AdvisoryModelAdjustments = new Endpoint() {
            @Override
            public String getPath()
                {
                    return "/v2/advisory/model/adjustments/{modelId}";
                }

            @Override
            public Class getDatatype()
                {
                    return JSONArray.class;
                }
        };

        public static final Endpoint AdvisoryModelRtbs = new Endpoint() {
            @Override
            public String getPath()
                {
                    return "/v2/advisory/model/rtbs/{modelId}";
                }

            @Override
            public Class getDatatype()
                {
                    return JSONArray.class;
                }
        };

        public static final Endpoint AdvisoryModelAdjustmentsModify = () -> "/v2/advisory/model/adjustments/modify/{action}";

        public static final Endpoint AdvisoryModelAdjustmentPreview = new Endpoint()
            {
                @Override
                public String getPath()
                    {
                        return "/v2/advisory/model/adjustments/preview/{adjustmentId}";
                    }

                @Override
                public Class getDatatype()
                    {
                        return JSONArray.class;
                    }
            };

        public static final Endpoint AdvisoryModelAdjustmentSchedule = () -> "/v2/advisory/model/adjustments/preallocate";

        public static final Endpoint AdvisoryModelAdjustmentTrigger = () -> "/v2/advisory/model/adjustments/allocation/trigger";

        public static final Endpoint AdvisoryModelAllocationCancel = () -> "/v2/advisory/model/adjustments/allocation/cancel";

        public static final Endpoint AdvisoryModelAllocate = () -> "/v2/advisory/model/orders/allocate";

        public static final Endpoint AdvisoryModelPlaceEquity = () -> "/orders/v2/advisory/equity/place";

        public static final Endpoint AdvisoryModelPreviewEquity = () -> "/orders/v2/advisory/equity/preview";

        public static final Endpoint OrderCancel = () -> "/orders/v2/cancel";

        public static final Endpoint AdvisoryModelPortfolios = () -> "/v2/advisory/model/portfolios/{modelId}";

        public static final Endpoint AdvisoryModelPerformance = new Endpoint() {
            @Override
            public String getPath()
                {
                    return "/v2/advisory/analytics/model/performance/{modelId}/{range}";
                }

            @Override
            public Class getDatatype()
                {
                    return JSONArray.class;
                }
        };

        public static final Endpoint AdvisoryModelBalance = () -> "/v2/advisory/model/rtb/{modelId}";

        public static final Endpoint AdvisoryModelBalanceHistory = new Endpoint() {
            @Override
            public String getPath()
                {
                    return "/v2/advisory/model/rtb/history/{modelId}";
                }

            @Override
            public Class getDatatype()
                {
                    return JSONArray.class;
                }
        };

        public static final Endpoint AdvisoryModels = new Endpoint() {
            @Override
            public String getPath()
                {
                    return "/v2/advisory/models";
                }

            @Override
            public Class getDatatype()
                {
                    return JSONArray.class;
                }
        };

        public static final Endpoint AdvisoryModelOrders = new Endpoint() {
            @Override
            public String getPath()
                {
                    return "/orders/model/{modelId}/{type}";
                }

            @Override
            public Class getDatatype()
                {
                    return JSONArray.class;
                }
        };

        public static final Endpoint AdvisoryModelAccountStats = () -> "/v2/advisory/model/accounts/stats/{modelId}";

        public static final Endpoint AdvisoryModelAccounts = new Endpoint() {
            @Override
            public String getPath()
                {
                    return "/v2/advisory/model/accounts/{modelId}";
                }

            @Override
            public Class getDatatype()
                {
                    return JSONArray.class;
                }
        };

        public static final Endpoint AdvisoryModelArphans = new Endpoint() {
            @Override
            public String getPath()
                {
                    return "/v2/advisory/model/accounts/orphaned";
                }

            @Override
            public Class getDatatype()
                {
                    return JSONArray.class;
                }
        };

        public static final Endpoint AdvisoryAllocation = new Endpoint() {
            @Override
            public String getPath()
                {
                    return "/v2/advisory/allocations/{allocationRef}";
                }

            @Override
            public Class getDatatype()
                {
                    return JSONArray.class;
                }
        };

        public static final Endpoint UserBalancesHistory = new Endpoint() {
            @Override
            public String getPath()
                {
                    return "/user/rtb/history";
                }

            @Override
            public Class getDatatype()
                {
                    return JSONArray.class;
                }
        };

        public static final Endpoint UserBuyingPower = () -> "/user/balance";

        public static final Endpoint UserBalance = () -> "/user/rtb";

        public static final Endpoint UserPortfolio = new Endpoint() {
            @Override
            public String getPath()
                {
                    return "/user/portfolio";
                }

            @Override
            public Class getDatatype()
                {
                    return JSONArray.class;
                }
        };

        public static final Endpoint UserPortfolioFull = () -> "/user/portfolio/full";

        public static final Endpoint UserPreferences = () -> "/user/preferences";

        public static final Endpoint UserPreferencesSet = () -> "/user/preferences/set";

        public static final Endpoint UserPreferencesDelete = () -> "/user/preferences/delete";

        public static final Endpoint OrdersCost = new Endpoint() {
            @Override
            public String getPath()
                {
                    return "/orders/cost";
                }

            @Override
            public Class getDatatype()
                {
                    return JSONArray.class;
                }
        };

        public static final Endpoint OrdersStatus = () -> "/orders/status/{orderRef}";
    }
