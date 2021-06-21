# Core

### Basic Dialogs

![](https://raw.githubusercontent.com/vanpra/compose-material-dialogs/main/imgs/basic_core.png)

Here is an example to make a basic dialog with a title and text:

```kotlin
val dialog = remember { MaterialDialog() }

dialog.build {
    title(text = "Use Location Services?")
    message(res = R.string.location_dialog_message)
}

/* This should be called in an onClick or an Effect */ 
dialog.show()
```

The dialog is shown when the `dialog.show()` is called. The hard coded strings for all the components in the example above can be replaced with a string resource id. 

If the `autoDismiss` parameter of a `MaterialDialog`  is set to false then the dialog will not close when the positive or negative buttons are clicked, allowing for flexibility in custom views.

The `MaterialDialog` class also has a `onCancelRequest` parameter which is a callback which is used when the user clicks outside the dialog area. By default this parameter is set to hide the dialog.

Buttons can be added to the dialog by adding the following:

```kotlin
dialog.build {
    ...
    
    buttons {
        positiveButton("Ok", onClick = { 
            //Do Something
        })
        
        button("Later", onClick = {
            //Do Something
        })
        
        negativeButton("Cancel", onClick = { 
            //Do Something
        })
    }
}
```

If the text of the buttons is too long to fit in one row the buttons will be automatically stacked on top on one another.

### Input Dialogs 

<img src="https://raw.githubusercontent.com/vanpra/compose-material-dialogs/main/imgs/input.jpg" width="300" height="600"  />

```kotlin
dialog.build {
    ...
   	input(label = "Name", hint = "Jon Smith") { inputString ->
        /* Do something */
    }
    buttons {
        ...
    }
	...
}
```

In the code snippet above creates the dialog seen in the image (without the title). Once an input is entered and the user presses the positive button, the dialog will callback with the string the user inputted.  The input function also has other parameters which can be found in the comment above the input function implementation.

### List Dialogs

<img src="https://raw.githubusercontent.com/vanpra/compose-material-dialogs/main/imgs/basic_list.jpg" width="300" height="600" />

Below is an example of a plain list dialog:

```kotlin
dialog.build {
    ...
    listItems(listOf("Item 1", "Item 2", "Item 3"), onClick { index, item ->
        //Do Something
    })
    ...
}
```

If you would like to use a custom list item layout you can use the following code:

```kotlin
data class TextIcon(text: String, icon: ImageAsset)
val items = listOf(
    TextIcon("Item 1", imageFromResource(R.drawable.item_one))
    TextIcon("Item 2", imageFromResource(R.drawable.item_two))
    TextIcon("Item 3", imageFromResource(R.drawable.item_three))
)

dialog.build {
    ...
    listItems(items, onClick { index, item ->
        //Do Something
    }) {
        Row {
            Box(Modifier.preferedSize(40.dp)) {
                Image(it.icon)
            }
            Text(it.text, modifier = Modifier.padding(start = 12.dp))
        }
    }
	...
}
```

### Single Choice List

![](https://raw.githubusercontent.com/vanpra/compose-material-dialogs/main/imgs/single_selection.png)

```kotlin
dialog.build {
    listItemsSingleChoice(
        list = listOf("Item 1", "Item 2", "Item 3"),
        disabledIndices = listOf(1),
        initialSelection = 2
    ) {
        //Do Something
    }
}
```

As seen in the code snippet above you can pass in a list of indices of items which should be disabled (ie. cannot be selected) and the item index which should selected to start with. By default, if no initial selection is given it will default to the first item.

### Multiple Choice List

![](https://raw.githubusercontent.com/vanpra/compose-material-dialogs/main/imgs/multi_selection.png)

```kotlin
dialog.build {
    listItemsSingleChoice(
        list = listOf("Item 1", "Item 2", "Item 3"),
        disabledIndices = listOf(1),
        initialSelection = listOf(1)
    ) {
        //Do Something
    }
}
```

As seen in the code snippet above you can select which items will be disabled (ie. cannot be selected) and also a list of item indicies which should be initially selected.

### Custom View

```kotlin
dialog.build {
    customView {
    	//Define your view here
    }
}
```

The custom view provides a box with the appropriate material design padding's  assigned to it so that your content. If you don't want the padding you can just put your composable anywhere in the `dialog.build` scope and it will be displayed as normal.

### Theming

The background colour of the dialog can be changed by using the `backgroundColor` parameter of the build function:

```kotlin
dialog.build(backgroundColor = Color.Red) {
    title(text = "Use Location Services?")
    message(res = R.string.location_dialog_message)
}
```

The shape of the dialog is dictated by the `MaterialTheme.shapes.medium` parameter.
