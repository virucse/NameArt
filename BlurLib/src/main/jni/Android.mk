LOCAL_PATH		:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := genius_blur
LOCAL_SRC_FILES := stackblur.c net_qiujuer_genius_blur_StackNative.c load.c
LOCAL_LDLIBS    :=-L$(SYSROOT)/usr/lib -lm -llog -ljnigraphics

include $(BUILD_SHARED_LIBRARY)