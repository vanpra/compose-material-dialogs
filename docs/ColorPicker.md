# Color Picker

![](https://raw.githubusercontent.com/vanpra/compose-material-dialogs/main/imgs/color_picker.png)

### Main Color Picker

Here is an example of how to add a color picker to a dialog:

``` kotlin
val dialog = remember { MaterialDialog() }

dialog.build {
    ...
    colorPicker(colors = ColorPalette.Primary)
    ...
}

/* This should be called in an onClick or an Effect */ 
dialog.show()
```

`ColorPalette.Primary` is a predefined list of colors and can be replaced with a list of custom colors.

### Sub Color Picker

Here is an example of how to add a color picker with sub colors to a dialog:

``` kotlin
dialog.build {
    ...
    colorPicker(colors = ColorPalette.Primary, subColors = ColorPalette.PrimarySub)
    ...
}
```

The `subColors` parameter is passed in a list of list of colors which are show to the user when they click on a color from `colors` list. These lists are matched by the order they appear in the list ie. the first color in `colors` matches with the first list in `subColors`

### ARGB Color Picker

Here is an example of how to add a color picker with custom argb selector to a dialog:

``` kotlin
dialog.build {
    ...
    colorPicker(colors = ColorPalette.Primary, allowCustomArgb = true)
    ...
}
```

