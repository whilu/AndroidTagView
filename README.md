# AndroidTagView

[![Build Status](https://travis-ci.org/whilu/AndroidTagView.svg)](https://travis-ci.org/whilu/AndroidTagView)

An Android TagView library. You can customize awesome TagView by using this library.

## Screenshots

<img src="/screenshots/androidtagview_record_1.gif" alt="androidtagview_record_1.gif" title="androidtagview_record_1.gif" width="400" height="660" />

## Usage

### Step 1

Add the dependency in your build.gradle.

```groovy
dependencies {
    compile 'co.lujun:androidtagview:1.0.0'
}
```

### Step 2

Use the AndroidTagView in layout file, you can add customized attributes here.

```xml
<co.lujun.androidtagview.TagContainerLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="0dp"
    android:padding="10dp"
    app:container_enable_drag="false"
    app:horizontal_interval="10dp"
    app:vertical_interval="10dp"
    app:tag_clickable="true"
    app:tag_theme="pure_teal" />
```

### Step 3

Use TagView in your code.

```java
TagContainerLayout mTagContainerLayout = (TagContainerLayout) findViewById(R.id.tagcontainerLayout);
mTagContainerLayout.setTags(List<String> tags);
```

Now, you have successfully created some TagViews. The following will show some more useful features for you customize.

## Attributes

|name|format|description|
|:---:|:---:|:---:|
| vertical_interval | dimension | Vertical interval, default 5(dp)
| horizontal_interval | dimension | Horizontal interval, default 5(dp)
| container_border_width | dimension | TagContainerLayout border width(default 0.5dp)
| container_border_radius | dimension | TagContainerLayout border radius(default 10.0dp)
| container_border_color | color | TagContainerLayout border color(default #22FF0000)
| container_background_color | color | TagContainerLayout background color(default #11FF0000)
| container_enable_drag | boolean | Can drag TagView(default false)
| container_drag_sensitivity | float | The sensitive of the ViewDragHelper(default 1.0f, normal)
| tag_border_width | dimension | TagView Border width(default 0.5dp)
| tag_corner_radius | dimension | TagView Border radius(default 15.0dp)
| tag_horizontal_padding | dimension | Horizontal padding for TagView, include left and right padding(left and right padding are equal, default 20px)
| tag_vertical_padding | dimension | Vertical padding for TagView, include top and bottom padding(top and bottom padding are equal, default 17px)
| tag_text_size | dimension | TagView Text size(default 14sp)
| tag_text_color | color | TagView text color(default #FF666666)
| tag_border_color | color | TagView border color(default #88F44336)
| tag_background_color | color | TagView background color(default #33F44336)
| tag_max_length | integer | The max length for TagView(default max length 23)
| tag_clickable | boolean | Whether TagView can clickable(default unclickable)
| tag_theme | enum | The TagView [theme](#Themes)

**You can set these attributes in layout file, or use setters(each attribute has get and set method) to set them.**

## <span id="Themes">Themes</span>

|theme|code|description
|:---:|:---:|:---:|
| none | ColorFactory.NONE | If you customize TagView with your way, set this theme
| random | ColorFactory.RANDOM | Create each TagView using random color
| pure_cyan | ColorFactory.PURE_CYAN | All TagView created by pure cyan color
| pure_teal | ColorFactory.PURE_TEAL | All TagView created by pure teal color


## <span id="Methods">Methods</span>

* Set a ```TagView.OnTagClickListener``` for TagView, for ```onTagClick``` and ```onTagLongClick``` callback
```java
mTagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {

    @Override
    public void onTagClick(int position, String text) {
    	// ...
    }

    @Override
    public void onTagLongClick(final int position, String text) {
        // ...
    }
});
```
* Set text max length for all TagView in ContainerLayout.
```java
mTagContainerLayout.setTagMaxLength(int max);
```
* Get TagView text at the specified location.
```java
String text = mTagContainerLayout.getTagText(int position);
```
* If you set the attribute ```container_enable_drag``` to ```true```, when drag the TagView you can get latest state use this method. There are 4 state:```ViewDragHelper.STATE_IDLE```, ```ViewDragHelper.STATE_DRAGGING```, and ```ViewDragHelper.STATE_SETTLING```.
```java
int state = mTagContainerLayout.getTagViewState();
```
* Set the [theme](#Themes). If you want to customize theme, remember set theme with ```ColorFactory.NONE``` first, then set other attributes.
```java
// Set library provides theme
mTagContainerLayout.setTheme(ColorFactory.PURE_CYAN);
```
```java
// Set customize theme
mTagContainerLayout1.setTheme(ColorFactory.NONE);
mTagContainerLayout1.setTagBackgroundColor(Color.TRANSPARENT);
```

**After set the attributes, set tags or add tags.**

* Use ```setTags()``` to set tags, require a parameter of type ```List<String>``` or ```String[]```.
```java
mTagContainerLayout.setTags(List<String> tags);
```
* Inserts a TagView into ContainerLayout at the end position.
```java
mTagContainerLayout.addTag(String text);
```
* Inserts a TagView into ContainerLayout at the specified location, the TagView is inserted before the current element at the specified location.
* ```java
mTagContainerLayout.addTag(String text, int position);
```
* Remove a TagView in specified position, require the positon of the TagView
```java
mTagContainerLayout.removeTag(int position);
```

## Log
###1.0.0
- First release

## Sample
[APK](/sample/sample-release.apk)

## About
If you have any questions, contact me: [lujun.byte#gmail.com](mailto:lujun.byte@gmail.com).

## License

    Copyright 2015 lujun

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.