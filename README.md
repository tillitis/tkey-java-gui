# TKeyJGUI
This is a work in progress GUI application which uses the [Tkey-jClient library](https://github.com/iknek/Tkey-jClient) to communicate with a [Tillitis Tkey](tillitis.se).

Serial USB communication is achieved through the use of the external library [jSerialComm](https://github.com/Fazecast/jSerialComm). The library jar is already placed in the root of this library folder.

## Usage
1. Clone this repo.
2. Add the two (included) library jars as libraries in your IDE of choice.
3. Run the app.

## Functionality
This program allows you to:
1. Connect to a TKey.
2. Get the TKey name & version
3. Get the TKey UDI and its associated information
4. Load an app to the TKey.

## To Do
1. Fix the Tkey-jClient library to allow for reconnection after TKey is reset, without re-starting the app.
2. 'Console' improvements, potentially including the possibility to generate and send frames to the TKey.
3. Code cleanup.
4. Documentation.
5. General GUI improvements & testing.
