# AndroidTagView

[![Build Status](https://travis-ci.org/whilu/AndroidTagView.svg)](https://travis-ci.org/whilu/AndroidTagView) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-AndroidTagView-green.svg?style=true)](https://android-arsenal.com/details/1/2992)

An Android TagView library. You can customize awesome TagView by using this library.

## Screenshots

<img src="/screenshots/androidtagview_record_1.gif" alt="androidtagview_record_1.gif" title="androidtagview_record_1.gif" width="400" height="660" /> <img src="/screenshots/device-2016-11-09-223523.png" alt="device-2016-11-09-223523.png" title="device-2016-01-16-233617.png" width="400" height="660" />

## Usage

### Step 1

Add below dependency in your **build.gradle** file.

```groovy
dependencies {
    compile 'co.lujun:androidtagview:1.1.7'
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
| container_gravity | enum | The TagContainerLayout [gravity](#gravity)
| container_max_lines | integer | The max lines for TagContainerLayout(default 0, auto increase)
| tag_border_width | dimension | TagView Border width(default 0.5dp)
| tag_corner_radius | dimension | TagView Border radius(default 15.0dp)
| tag_horizontal_padding | dimension | Horizontal padding for TagView, include left and right padding(left and right padding are equal, default 10dp)
| tag_vertical_padding | dimension | Vertical padding for TagView, include top and bottom padding(top and bottom padding are equal, default 8dp)
| tag_text_size | dimension | TagView Text size(default 14sp)
| tag_bd_distance | dimension | The distance between baseline and descent(default 2.75dp)
| tag_text_color | color | TagView text color(default #FF666666)
| tag_border_color | color | TagView border color(default #88F44336)
| tag_background_color | color | TagView background color(default #33F44336)
| tag_max_length | integer | The max length for TagView(default max length 23)
| tag_clickable | boolean | Whether TagView can clickable(default false)
| tag_selectable | boolean | Whether TagView can be selectable(default false)
| tag_theme | enum | The TagView [theme](#themes)
| tag_text_direction | enum | The TagView text [direction](#directions)
| tag_ripple_color | color | The ripple effect color(default #EEEEEE)
| tag_ripple_alpha | integer | The ripple effect color alpha(the value may between 0 - 255, default 128)
| tag_ripple_duration | integer | The ripple effect duration(In milliseconds, default 1000ms)
| tag_enable_cross | boolean | Enable draw cross icon(default false)
| tag_cross_width | dimension | The cross area width(your cross click area, default equal to the TagView's height)
| tag_cross_color | color | The cross icon color(default Color.BLACK)
| tag_cross_line_width | dimension | The cross line width(default 1dp)
| tag_cross_area_padding | dimension | The padding of the cross area(default 10dp)
| tag_support_letters_rlt | boolean | Whether to support 'letters show with RTL(eg: Android -> diordnA)' style(default false)
| tag_background | reference | TagView background resource(default none background)

**You can set these attributes in layout file, or use setters(each attribute has get and set method) to set them.**

## <span id="themes">Themes</span>

|theme|code|value|description
|:---:|:---:|:---:|:---:|
| none | ColorFactory.NONE | -1 | **If you customize TagView with your way, set this theme**
| random | ColorFactory.RANDOM | 0 | Create each TagView using random color
| pure_cyan | ColorFactory.PURE_CYAN | 1 | All TagView created by pure cyan color
| pure_teal | ColorFactory.PURE_TEAL | 2 | All TagView created by pure teal color

## <span id="directions">Directions</span>

|direction|code|value|description
|:---:|:---:|:---:|:---:|
| ltr | View.TEXT_DIRECTION_LTR | 3 | Text direction is forced to LTR(default)
| rtl | View.TEXT_DIRECTION_RTL | 4 | Text direction is forced to RTL

## <span id="gravity">Gravity</span>

|gravity|code|value|description
|:---:|:---:|:---:|:---:|
| left | Gravity.LEFT | 3 | Push TagView to the left of TagContainerLayout(default)
| center | Gravity.CENTER | 17 | Push TagView to the center of TagContainerLayout
| right | Gravity.RIGHT | 5 | Push TagView to the right of TagContainerLayout

## <span id="Methods">Methods</span>

* Set a ```TagView.OnTagClickListener``` for TagView, for ```onTagClick``` , ```onTagLongClick``` and ```onTagCrossClick``` callback
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

    @Override
    public void onSelectedTagDrag(int position, String text){
        // ...
    }
    
    @Override
    public void onTagCrossClick(int position) {
        // ...
    }
});
```
* Use ```setTagMaxLength(int max)``` to set text max length for all TagView.
```java
mTagContainerLayout.setTagMaxLength(int max);
```
* Use ```getTagText(int position)``` to get TagView text at the specified location.
```java
String text = mTagContainerLayout.getTagText(int position);
```
* ```getTags()``` return a string list for all tags in TagContainerLayout.
```java
List<String> list = mTagContainerLayout.getTags();
```
* If you set the attribute ```container_enable_drag``` to ```true```, when drag the TagView you can get latest state by using ```getTagViewState()```. There are 4 state:```ViewDragHelper.STATE_IDLE```, ```ViewDragHelper.STATE_DRAGGING```, and ```ViewDragHelper.STATE_SETTLING```.
```java
int state = mTagContainerLayout.getTagViewState();
```
* Set the [theme](#themes). If you want to customize theme, remember set theme with ```ColorFactory.NONE``` first, then set other attributes.
```java
// Set library provides theme
mTagContainerLayout.setTheme(ColorFactory.PURE_CYAN);
```
```java
// Set customize theme
mTagContainerLayout.setTheme(ColorFactory.NONE);
mTagContainerLayout.setTagBackgroundColor(Color.TRANSPARENT);
```
* Set the text [direction](#directions). The library support two direction ```View.TEXT_DIRECTION_LTR``` and ```View.TEXT_DIRECTION_RTL```.
```java
mTagContainerLayout.setTagTextDirection(View.TEXT_DIRECTION_RTL);
```
* Use ```setTagTypeface(Typeface typeface)``` to set TagView text typeface.
```java
Typeface typeface = Typeface.createFromAsset(getAssets(), "iran_sans.ttf");
mTagContainerLayout.setTagTypeface(typeface);
```

**After set the attributes, set tags or add a tag.**

* Use ```setTags()``` to set tags, require a parameter of type ```List<String>``` or ```String[]```.
```java
mTagContainerLayout.setTags(List<String> tags);
```
* Insert a TagView into ContainerLayout at the end.
```java
mTagContainerLayout.addTag(String text);
```
* Insert a TagView into ContainerLayout at the specified location, the TagView is inserted before the current element at the specified location.
```java
mTagContainerLayout.addTag(String text, int position);
```
* Remove TagView on particular position, require the position of the TagView.
```java
mTagContainerLayout.removeTag(int position);
```
* Remove all TagViews.
```java
mTagContainerLayout.removeAllTags();
```
* Get a TagView in specified position.
```java
mTagContainerLayout.getTagView(int position);
```
* Set color for each TagView.
```java
List<int[]> colors = new ArrayList<int[]>();
//int[] color = {TagBackgroundColor, TabBorderColor, TagTextColor}
int[] color1 = {Color.RED, Color.BLACK, Color.WHITE};
int[] color2 = {Color.BLUE, Color.BLACK, Color.WHITE};
colors.add(color1);
colors.add(color2);
mTagcontainerLayout.setTags(tags, colors);
```

## Change logs

### 1.1.7(2019-01-21)
- Fix bugs

### 1.1.6(2018-12-1)
- Support tag selectable

### 1.1.5(2018-8-20)
- Allow images on tags (in LTR languages).

### 1.1.4(2017-6-1)
- Add attribute for TagView background.

### 1.1.3(2017-5-17)
- Add ```getTagView(int position)``` method to get TagView in specified position.

### 1.1.2(2017-5-16)
- Fix bugs

### 1.1.1(2017-4-16)
- Customize the color of the TagView, see [#51](https://github.com/whilu/AndroidTagView/pull/51)
- Fixed issue [#50](https://github.com/whilu/AndroidTagView/issues/50), [#49](https://github.com/whilu/AndroidTagView/issues/49)

### 1.1.0(2017-3-5)
- Fixed issue [#45](https://github.com/whilu/AndroidTagView/issues/45)
- Support 'letters show with RTL(eg: Android -> diordnA)' style

### 1.0.6(2017-2-14)
- Fix bugs

### 1.0.5(2016-11-9)
- Add cross view for TagView

### 1.0.4(2016-10-30)
- Support ripple effect(Call requires API level 11), like [Android CustomButton](https://github.com/whilu/AndroidSample/tree/master/CustomButton)
- Fix bugs

### 1.0.3(2016-4-3)
- Add ```getTags()``` method to get the list for all tags
- Fixed bugs in ListView/RecyclerView

### 1.0.2(2016-1-18)
- Support [gravity](#gravity) for ```TagContainerLayout```
- Support set typeface

### 1.0.1(2016-1-14)
- Support text [direction](#directions)
- Add ```removeAllTags()``` method for remove all TagViews
- Fixed issue [#1](https://github.com/whilu/AndroidTagView/issues/1)
- Fixed other bugs

### 1.0.0(2016-1-6)
- First release

## Sample App
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
