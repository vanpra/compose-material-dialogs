# Changelog

### 0.4.2 - 2021-06-06

  -  Update compose to 1.0.0-beta08 ([#76](https://github.com/vanpra/compose-material-dialogs/issues/76) and [#73](https://github.com/vanpra/compose-material-dialogs/issues/73))
-  Update kotlin version to 1.5.10
-  Fix layout of date and time pickers for wider screens ([#74](https://github.com/vanpra/compose-material-dialogs/issues/74))
-  Fix dialog padding to conform with material design spec at the extremes
-  Add auto text field focusing on dialog open ([#67](https://github.com/vanpra/compose-material-dialogs/issues/67))

### 0.4.1 - 2021-05-23

  - Update compose version to 1.0.0-beta07
  - Fix date picker bounds bug
- Fix time picker custom titles

### 0.4.0 - 2021-05-06

  - Update compose version to 1.0.0-beta06
  - Add more style customisation's to date and time pickers
  - Fix date picker selection bug
  - Make performance optimisations to the date picker


### 0.3.4 - 2021-04-22

  - Update compose version to 1.0.0-beta05
  - Switch to using `HorizontalPager` from the Accompanist library for date picker
  - Add `keyboardActions` parameter to the input component

### 0.3.3 - 2021-04-08

  - Update compose version to 1.0.0-beta04
  - Modify dialog styling (elevation, shape, etc) to better match material spec (thanks **[ivaniskandar](https://github.com/ivaniskandar)**)
  - Fix issues when selection the hour 12 in the time picker
  - New time picker features (thanks **[Jahor](https://github.com/Jahor)** )
      - Add min and max time range/limit
      - Add optional 24 hour time selector
- Switch to publishing to maven central due to sun-setting of bintray 

### 0.3.2 - 2021-03-24

  - Update compose version to 1.0.0-beta03
  - Fix callback and selection bugs in color picker
  - Make alpha slider optional in color picker and add a tiled background to the ARGB hex label box (thanks **[ivaniskandar](https://github.com/ivaniskandar)**)
  - Remove predefined buttons for date and time picker components
  - Make custom list items padding be added after clickable to make ripple fill the max width

### 0.3.1 - 2021-03-10

  - Update compose version to 1.0.0-beta02
  - Fix input component bug and re-add it to the core module

### 0.3.0 - 2021-03-01

  - Update compose version to 1.0.0-beta01
  - Redesign date picker and time picker to match the material specification ([material.io]())
    - Introduce year picker
    - Add `colors` attributes to the time picker dialog to allow for more customisation (see time picker documentation)
    - Add am/pm toggle instead of using 24 hour time 
    - Known Issue: calendar view can stutter when swiping between months
  - Temporarily remove `input` dialog component due to an unknown internal bug. This will be re-added as soon as a fix/workaround can be found and `customView` can be used in the meantime.
  - Add `button` function to the `MaterialDialogButtons` class which should be used when the button action is neutral. This button will be displayed in-between any `positive` and `negative` buttons which are being used

### 0.2.11 - 2021-01-14

  - Fix issues with disabling positive button for the `input` and `listItemsSingleChoice` components
  - Fix bug lifecycle bug for repeated callbacks on subsequent uses of the same dialog
  - Add `keyboardOptions` parameter to `input` component
  - Remove `allowEmpty` parameter from `input` component in favour of using `isTextValid` parameter to do this validation
  - Add `disablePositiveButton` and `enablePositiveButton` methods to the `MaterialDialog` class to allow for further customisation of the dialog when using custom layouts

### 0.2.10  - 2021-01-13

  - Updated compose version to 1.0.0-alpha10
  - Add `onCloseRequest` parameter to the `MaterialDialog` constructor

### 0.2.9  - 2021-01-08

  - Update usages of remember to onCommit
  - Fix concurrency issues during button creation

### 0.2.8  - 2020-12-17

  - Updated compose version to 1.0.0-alpha09
  - Fix bug in input dialog component which caused callback to be called multiple times
  - Add documentation and code sample for input dialog component

### 0.2.7  - 2020-12-05

  - Updated compose version to 1.0.0-alpha08

### 0.2.6  - 2020-11-11

  - Updated compose version to 1.0.0-alpha07
  - Fix core module manifest to fix errors

### 0.2.5  - 2020-10-28

  - Updated compose version to 1.0.0-alpha06

### 0.2.4  - 2020-10-15

  - Updated compose version to 1.0.0-alpha05
  - Fixed crash when dialog button values such as text are changed
  - Clean up gradle files

### 0.2.3  - 2020-10-02

  - Updated compose version to 1.0.0-alpha04
  - Add optimisations to date picker 
  - Fix ARGB color picker layout

### 0.2.2  - 2020-09-16

  - Updated compose version to 1.0.0-alpha03

### 0.2.1  - 2020-09-02

  - Updated compose version to 1.0.0-alpha02

### 0.2.0  - 2020-08-26

  - Updated compose version to 1.0.0-alpha01

### 0.1.8  - 2020-08-19

  - Updated compose version to dev17
  - Updated kotlin version to 1.4.0
  - Fix date dialog and time dialog to also fit on smaller screens

### 0.1.6  - 2020-08-05

  - Updated compose version to dev16
  - Updated kotlin version to 1.4.0-rc
  - Fix datetime dialog to fit on smaller screens

### 0.1.5  - 2020-07-23

  - Updated compose version to dev15
  - Updated kotlin version to 1.4-M3
  - Make dialog button text uppercase

