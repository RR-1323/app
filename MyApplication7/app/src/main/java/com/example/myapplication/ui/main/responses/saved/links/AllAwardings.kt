package com.example.myapplication.ui.main.responses.saved.links

import com.google.gson.annotations.SerializedName


data class AllAwardings (

  @SerializedName("giver_coin_reward"                    ) var giverCoinReward                  : String?                       = null,
  @SerializedName("subreddit_id"                         ) var subredditId                      : String?                       = null,
  @SerializedName("is_new"                               ) var isNew                            : Boolean?                      = null,
  @SerializedName("days_of_drip_extension"               ) var daysOfDripExtension              : String?                       = null,
  @SerializedName("coin_price"                           ) var coinPrice                        : Int?                          = null,
  @SerializedName("id"                                   ) var id                               : String?                       = null,
  @SerializedName("penny_donate"                         ) var pennyDonate                      : String?                       = null,
  @SerializedName("award_sub_type"                       ) var awardSubType                     : String?                       = null,
  @SerializedName("coin_reward"                          ) var coinReward                       : Int?                          = null,
  @SerializedName("icon_url"                             ) var iconUrl                          : String?                       = null,
  @SerializedName("days_of_premium"                      ) var daysOfPremium                    : Int?                          = null,
  @SerializedName("tiers_by_required_awardings"          ) var tiersByRequiredAwardings         : String?                       = null,
  @SerializedName("resized_icons"                        ) var resizedIcons                     : ArrayList<ResizedIcons>       = arrayListOf(),
  @SerializedName("icon_width"                           ) var iconWidth                        : Int?                          = null,
  @SerializedName("static_icon_width"                    ) var staticIconWidth                  : Int?                          = null,
  @SerializedName("start_date"                           ) var startDate                        : String?                       = null,
  @SerializedName("is_enabled"                           ) var isEnabled                        : Boolean?                      = null,
  @SerializedName("awardings_required_to_grant_benefits" ) var awardingsRequiredToGrantBenefits : String?                       = null,
  @SerializedName("description"                          ) var description                      : String?                       = null,
  @SerializedName("end_date"                             ) var endDate                          : String?                       = null,
  @SerializedName("sticky_duration_seconds"              ) var stickyDurationSeconds            : String?                       = null,
  @SerializedName("subreddit_coin_reward"                ) var subredditCoinReward              : Int?                          = null,
  @SerializedName("count"                                ) var count                            : Int?                          = null,
  @SerializedName("static_icon_height"                   ) var staticIconHeight                 : Int?                          = null,
  @SerializedName("name"                                 ) var name                             : String?                       = null,
  @SerializedName("resized_static_icons"                 ) var resizedStaticIcons               : ArrayList<ResizedStaticIcons> = arrayListOf(),
  @SerializedName("icon_format"                          ) var iconFormat                       : String?                       = null,
  @SerializedName("icon_height"                          ) var iconHeight                       : Int?                          = null,
  @SerializedName("penny_price"                          ) var pennyPrice                       : String?                       = null,
  @SerializedName("award_type"                           ) var awardType                        : String?                       = null,
  @SerializedName("static_icon_url"                      ) var staticIconUrl                    : String?                       = null

)