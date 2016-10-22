# Camera Wrapper with GLSurfaceView for android

Android introduced Camera2 in API level 21 and its dificult to support old devices and new devices in one app. The way the new API works is vastly different from the old one. Which sometimes mean implementing two independent pathways for both the cameras.

This is an attempt to wrap the two APIs in one Class and give an API for common use cases.

This comes with a GLSurfaceView for preview surface as I find it the most versatile (for adding OpenGLES shaders etc)

## Current supported functions :
1. Open - opens a camera (defaults to front facing) and begins previewing on the GLSurfaceView
2. Swap Camera - flips between front facing and rear facing camera

## Usage
1. initialize camera
2. set Renderer for your GLSurfaceView
