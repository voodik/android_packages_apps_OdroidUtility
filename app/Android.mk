LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_PACKAGE_NAME := OdroidUtility
LOCAL_PRIVATE_PLATFORM_APIS := true
LOCAL_PRIVILEGED_MODULE := true
LOCAL_CERTIFICATE := platform
LOCAL_MODULE_TAGS := optional

LOCAL_MANIFEST_FILE := src/main/AndroidManifest.xml

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_STATIC_JAVA_LIBRARIES := \
    common-codec \
    android-support-v4 \
    androidx.legacy_legacy-support-v4 \
    androidx.core_core \
    androidx-constraintlayout_constraintlayout \
    androidx.appcompat_appcompat \
    androidx.preference_preference \
    androidx.legacy_legacy-preference-v14 \
    androidx.recyclerview_recyclerview \

#    com.google.android.material_material

LOCAL_STATIC_JAVA_AAR_LIBRARIES  := \
    firebase-analytics \
    firebase-analytics-impl \
    firebase-common \
    firebase-core \
    firebase-iid \
    firebase-iid-interop \
    firebase-measurement-connector \
    firebase-measurement-connector-impl \
    play-services-ads-identifier \
    play-services-base \
    play-services-basement \
    play-services-measurement-api \
    play-services-measurement-base \
    play-services-stats \
    play-services-tasks \
	androidx-material

LOCAL_RESOURCE_DIR := \
    $(LOCAL_PATH)/src/main/res \
    frameworks/support/v7/appcompat/res \
    frameworks/support/v7/recyclerview/res \
    frameworks/support/cardview/res \
    frameworks/support/coordinatorlayout/src/main/res \
    frameworks/support/preference/res \



LOCAL_AAPT_FLAGS := --auto-add-overlay
LOCAL_AAPT_FLAGS += --extra-packages android.support.v4
LOCAL_AAPT_FLAGS += --extra-packages androidx.legacy.v4
LOCAL_AAPT_FLAGS += --extra-packages androidx.core
LOCAL_AAPT_FLAGS += --extra-packages androidx.annotation
LOCAL_AAPT_FLAGS += --extra-packages androidx.appcompat
LOCAL_AAPT_FLAGS += --extra-packages androidx.constraintlayout.widget
LOCAL_AAPT_FLAGS += --extra-packages androidx.preference
LOCAL_AAPT_FLAGS += --extra-packages androidx.legacy.preference
LOCAL_AAPT_FLAGS += --extra-packages androidx.recyclerview
LOCAL_AAPT_FLAGS += --extra-packages com.google.android.material
LOCAL_AAPT_FLAGS += --extra-packages com.google.firebase.measurement
LOCAL_AAPT_FLAGS += --extra-packages com.google.firebase.measurement_impl
LOCAL_AAPT_FLAGS += --extra-packages com.google.firebase
LOCAL_AAPT_FLAGS += --extra-packages com.google.firebase.firebase_core
LOCAL_AAPT_FLAGS += --extra-packages com.google.firebase.iid
LOCAL_AAPT_FLAGS += --extra-packages com.google.firebase.iid.internal
LOCAL_AAPT_FLAGS += --extra-packages com.google.firebase.analytics.connector
LOCAL_AAPT_FLAGS += --extra-packages com.google.firebase.analytics.connector.impl
LOCAL_AAPT_FLAGS += --extra-packages com.google.android.gms.ads_identifier
LOCAL_AAPT_FLAGS += --extra-packages com.google.android.gms.base
LOCAL_AAPT_FLAGS += --extra-packages com.google.android.gms.common
LOCAL_AAPT_FLAGS += --extra-packages com.google.android.gms.measurement.api
LOCAL_AAPT_FLAGS += --extra-packages com.google.android.gms.measurement_base
LOCAL_AAPT_FLAGS += --extra-packages com.google.android.gms.stats
LOCAL_AAPT_FLAGS += --extra-packages com.google.android.gms.tasks

LOCAL_PROGUARD_ENABLED := disabled
LOCAL_JAR_EXCLUDE_FILES := none
LOCAL_JAVA_LANGUAGE_VERSION := 1.7
#LOCAL_DX_FLAGS := --multi-dex --main-dex-list=$(mainDexList) --minimal-main-dex
#LOCAL_JACK_FLAGS += --multi-dex native
include $(BUILD_PACKAGE)

##################################################
include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := optional

LOCAL_PROGUARD_ENABLED := disabled

LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := \
    common-codec:libs/commons-codec-1.11-rep.jar \
    firebase-analytics:libs/firebase-analytics-16.0.3.aar \
    firebase-analytics-impl:libs/firebase-analytics-impl-16.2.1.aar \
    firebase-common:libs/firebase-common-16.0.1.aar \
    firebase-core:libs/firebase-core-16.0.3.aar \
    firebase-iid:libs/firebase-iid-16.0.0.aar \
    firebase-iid-interop:libs/firebase-iid-interop-16.0.0.aar \
    firebase-measurement-connector:libs/firebase-measurement-connector-17.0.0.aar \
    firebase-measurement-connector-impl:libs/firebase-measurement-connector-impl-17.0.1.aar \
    play-services-ads-identifier:libs/play-services-ads-identifier-15.0.1.aar \
    play-services-base:libs/play-services-base-15.0.1.aar \
    play-services-basement:libs/play-services-basement-15.0.1.aar \
    play-services-measurement-api:libs/play-services-measurement-api-16.0.1.aar \
    play-services-measurement-base:libs/play-services-measurement-base-16.0.2.aar \
    play-services-stats:libs/play-services-stats-15.0.1.aar \
    play-services-tasks:libs/play-services-tasks-15.0.1.aar \
    androidx-material:libs/material-1.1.0-alpha01.aar \

LOCAL_JAR_EXCLUDE_FILES := none
LOCAL_JAVA_LANGUAGE_VERSION := 1.7
include $(BUILD_MULTI_PREBUILT)
