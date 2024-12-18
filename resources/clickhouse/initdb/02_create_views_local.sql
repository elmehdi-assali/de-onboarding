








CREATE TABLE views_local
(
  sign Int8, --field needed for the CollapsingMergeTree
  
  session_date Date, --date with a precision of day of the view creation in the user's app. This field is use for partition and sort key
  project_id UInt32,
  user_id UInt64,
  session_id UInt64,
  device_id UInt8,
  session_number UInt16,
  session_time DateTime, --date with a precision of seconds of the session creation in the user's app
  screen_height UInt16,
  screen_width UInt16,
  session_number_of_views UInt16, --number of views for the session of this view
  session_duration_msec UInt32, -- duration of the session in milliseconds
  language LowCardinality(String), -- ISO 639
  city LowCardinality(String),
  country_code LowCardinality(String),
  dynamic_vars_string_in_session Nested -- union of all the dynamic vars in the views of this session
  (
    key String,
    value String
  ),
  dynamic_vars_int_in_session Nested -- union of all the dynamic vars in the views of this session
  (
    key String,
    value UInt32
  ),
  sessionizer_version LowCardinality(String),
  inserter_version LowCardinality(String),
  ---------------
  -- transactions
  ---------------
  session_number_of_transactions UInt16,
  session_transactions_revenue_cents UInt64,-- sum of the revenue of all transactions for the session of this view
  -------------------------------------
  -- transactions for the whole session
  -------------------------------------
  session_transactions Nested
  (
    id String,
    transaction_time DateTime,
    revenue_cents UInt64,
    currency UInt16, --ISO 4217
    transaction_view_number UInt32
  ),
  session_add_to_cart_items Nested
  (
    sku String,
    catalog_id UInt32,
    product_id_hashed UInt64,
    is_in_transaction UInt8,
    add_to_cart_time DateTime,
    add_to_cart_view_number UInt32
  ),
  ---------------------------------------------------------------------------------
  -- precomputed fields when the data is available before inserting into clickhouse
  ---------------------------------------------------------------------------------
  session_precomputed_aliases Nested --aliases of all the views of the session
  (
    alias_id UInt64
  ),
  session_precomputed_goals Nested
  (
    goal_id UInt64
  ),
  ------------------
  -- web only fields
  ------------------
  browser_name LowCardinality(String), --Normalized in moruscant, we would not benefit from dictionaries since they only work with uint64 keys
  browser_major_version UInt16,
  browser_version LowCardinality(String),
  platform_name LowCardinality(String),
  platform_version LowCardinality(String),
  referer_url String,
  custom_vars_session Nested
  (
    position UInt8,
    key String,
    value String
  ),
  custom_vars_in_session Nested -- union of all the custom vars in the views of this session
  (
    position UInt8,
    key String,
    value String
  ),
  ab_test_info Nested --contains all ab tests the sessions was in
  (
    abtest_id UInt32, -- TODO : if this field is not used for internal purposes, we must delete it from the schema
    abtest_version UInt16
  ),
  external_segments Nested
  (
    external_id String,
    integration_id UInt32,
    external_hashed_id UInt64,
    provider UInt8 --Provider is normalized in moruscant. Dictionaries would cost too much since they work with uint64
  ),
  has_playback_recorded UInt8, // Boolean
  session_products_visited Nested (
    catalog_id UInt32,
    product_id_hashed UInt64
  ),
  ------------------
  -- mobile only fields
  ------------------
  density Float32,
  session_connectivity_types UInt8, --stores the connections that happened during this session as bit. This field is queried using bit masking: NO_CONNECTION => 00000001, WIFI => 00000010, 2G => 00000100, 3G => 00001000, 4G => 00010000, 5G => 00100000
  os_version UInt64,
  app_version UInt64,
  sdk_build_number UInt64,
  manufacturer LowCardinality(String),
  model LowCardinality(String),
  app_events Nested
  (
  event_type UInt8,
  event_time DateTime,
  event_name LowCardinality(String),
  view_number UInt32
  ),

  view_number UInt16,
  view_time DateTime, --here for debuging
  scheme LowCardinality(String),
  prefix LowCardinality(String),
  path LowCardinality(String),
  query String,
  product_page_catalog_id UInt32,
  product_page_product_id_hashed UInt64,
  view_duration_msec UInt32,
  is_first UInt8, -- different from page_number = 1 since some session legitimately start with a page_number > 1
  is_last UInt8,
  dynamic_vars_string_view Nested
  (
    key String,
    value String
  ),
  dynamic_vars_int_view Nested
  (
    key String,
    value UInt32
  ),
  ---------------------------------------------------------------------------------
  -- precomputed fields when the data is available before inserting into clickhouse
  ---------------------------------------------------------------------------------
  precomputed_aliases Nested
  (
    alias_id UInt64
  ),
  -------------------------------
  -- fields used for retro-bg requests
  -------------------------------
  user_id_tag String, -- user id used to recompute pagekey and sessionKey for retroactive-background
  ------------------------------------------------------------
  -- list of targets by event for zoning and goals computation
  ------------------------------------------------------------
  targets_tap Nested
  (
    sum UInt16,
    path String,
    hashed_path UInt64,
    time_before_msec UInt32,
    responsive UInt8,
    orientation UInt8
  ),
  ------------------
  -- web only fields
  ------------------
  displays Nested (
    catalog_id UInt32,
    product_id_hashed UInt64,
    target_hashed_path UInt64,
    target_path String
  ),
  session_transaction_items Nested
  (
    catalog_id UInt32,
    product_id_hashed UInt64,
    revenue_cents UInt64,
    quantity UInt16,
    transaction_time DateTime,
    transaction_view_number UInt32
  ),
  custom_vars_view Nested
  (
    position UInt8,
    key String,
    value String
  ),
  window_height UInt16,
  window_width UInt16,
  document_height UInt32,
  document_width UInt16,
  targets_clicks Nested
  (
    sum UInt16,
    path String,
    hashed_path UInt64,
    time_before_msec UInt32,
    time_between_last_hover_and_first_click UInt32,
    catalog_id UInt32,
    product_id_hashed UInt64
  ),
  targets_hover Nested
  (
    sum UInt16,
    sum_times UInt32,
    path String,
    hashed_path UInt64,
    time_before_msec UInt32,
    catalog_id UInt32,
    product_id_hashed UInt64
  ),
  viewports Nested --shows the screen position for each scroll or resize on a web page
  (
    top_screen_position UInt16,
    bottom_screen_position UInt16,
    exposition_time_msec UInt32
  ),
  page_interaction_time_msec UInt32,
  scroll_rate Nullable(Float32),
  dom_interactive_after_msec Nullable(UInt32),
  fully_loaded Nullable(UInt32),
  start_render Nullable(UInt32),
  has_all_interaction_events UInt8, -- boolean
  last_hover_path String,
  view_playback_type UInt8,
  grey_recording_reason UInt16,
  etr_events Array(String),
  page_events Array(String),
  raw_clicks Nested -- we’re gonna keep raw_clicks to store different event, because the rename method in CH wasn't available
  (
    target_path String,
    event_time DateTime,
    event_type UInt8 -- same as interaction event type
  ),
  targets_focus Nested
  (
    sum UInt16,
    path String,
    hashed_path UInt64,
    time_before_msec UInt32
  ),
  ------------------
  -- mobile only fields
  ------------------
  targets_drag Nested
  (
    sum UInt16,
    path String,
    hashed_path UInt64,
    finger_direction UInt8,
    time_before_msec UInt32,
    orientation UInt8
  ),
  targets_flick Nested
  (
    sum UInt16,
    path String,
    hashed_path UInt64,
    finger_direction UInt8,
    time_before_msec UInt32,
    orientation UInt8
  ),
  targets_drag_and_flick Nested
  (
    sum UInt16,
    path String,
    hashed_path UInt64,
    finger_direction UInt8,
    orientation UInt8
  ),
  targets_double_tap Nested
  (
    sum UInt16,
    path String,
    hashed_path UInt64,
    orientation UInt8
  ),
  targets_long_press Nested
  (
    sum UInt16,
    path String,
    hashed_path UInt64,
    time_before_msec UInt32,
    orientation UInt8
  ),
  screen_name LowCardinality(String),
  mm_heatmap Nested --stores the relative position to a target. Mouse move heatmap prefixed with mm_.
  (
    x_rel UInt16,
    y_rel UInt16,
    nb_occurrences UInt8,
    target_hash UInt32
  ),
  errors Nested -- stores the errors that may happen during a page
  (
   error_type LowCardinality(String),
   error_time DateTime,
   message String,
   filename String,
   line_number UInt32,
   col_number UInt32
  )
) ENGINE = CollapsingMergeTree(sign)
PARTITION BY (session_date)
ORDER BY (project_id, device_id, user_id, session_id, view_number)
SAMPLE BY user_id
SETTINGS index_granularity=67336
;
