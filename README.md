# TKeyJGUI
This is a GUI application which uses the [tkey-javaclient library](https://github.com/tillitis/tkey-javaclient) to communicate with a [Tillitis TKey](https://www.tillitis.se). It allows for both simple operations such as getting the device UDI and name, loading apps, in addition to being a GUI tool for signing files and getting the TKey's public key.

## Licenses and SPDX tags

Unless otherwise noted, the project sources are licensed under the
terms and conditions of the "GNU General Public License v2.0 only":

> Copyright Tillitis AB.
>
> These programs are free software: you can redistribute it and/or
> modify it under the terms of the GNU General Public License as
> published by the Free Software Foundation, version 2 only.
>
> These programs are distributed in the hope that they will be useful,
> but WITHOUT ANY WARRANTY; without even the implied warranty of
> MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
> General Public License for more details.

> You should have received a copy of the GNU General Public License
> along with this program. If not, see:
>
> https://www.gnu.org/licenses

See [LICENSE](LICENSE) for the full GPLv2-only license text.

External source code we have imported are isolated in their own
directories. They may be released under other licenses. This is noted
with a similar `LICENSE` file in every directory containing imported
sources.

The project uses single-line references to Unique License Identifiers
as defined by the Linux Foundation's [SPDX project](https://spdx.org/)
on its own source files, but not necessarily imported files. The line
in each individual source file identifies the license applicable to
that file.

The current set of valid, predefined SPDX identifiers can be found on
the SPDX License List at:

https://spdx.org/licenses/

## Usage
1. Clone this repo.
2. Download and add the TKey Java library jar to the project.
3. Run the app using ./gradlew run.

## Functionality
This program allows you to:
1. Connect to a TKey.
2. Get the TKey name & version
3. Get the TKey UDI and its associated information
4. Load an app to the TKey.
5. Sign files and get TKey public key.

## To Do
1. Fix the jClient library to allow for reconnection after TKey is reset, without re-starting the app.
2. 'Console' improvements, potentially including the possibility to generate and send frames to the TKey.