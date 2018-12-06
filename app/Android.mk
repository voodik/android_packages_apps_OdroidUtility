LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_PACKAGE_NAME := OdroidUtility
LOCAL_PRIVATE_PLATFORM_APIS := true
LOCAL_CERTIFICATE := platform
LOCAL_MODULE_TAGS := optional
LOCAL_USE_AAPT2 := true

LOCAL_MANIFEST_FILE := src/main/AndroidManifest.xml

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_STATIC_ANDROID_LIBRARIES := \
    android-support-v4 \
    android-support-v7-appcompat \
    android-support-v7-preference \
    android-support-v7-recyclerview \
    android-support-design

LOCAL_STATIC_JAVA_LIBRARIES := \
    common-codec


  LOCAL_RESOURCE_DIR := \
      $(LOCAL_PATH)/src/main/res \
      frameworks/support/v7/preference/res \
      frameworks/support/design/res

LOCAL_PROGUARD_ENABLED := disabled

include $(BUILD_PACKAGE)

##################################################
include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := optional

LOCAL_PROGUARD_ENABLED := disabled

LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := \
    common-codec:libs/commons-codec-1.11-rep.jar

include $(BUILD_MULTI_PREBUILT)
