This project was crerated from TeamCity plugin template.
See https://github.com/jonnyzzz/TeamCity.PluginTemplate

In this example I show how to filter hidden property values in TeamCity. 
Of course it's always better to simply define password-typed parameter with TeamCity UI.


This is a IDEA project of server-side only plugin for TeamCity
For background information checkout any open-sourced plugins or see http://confluence.jetbrains.net/display/TCD65/Developing+TeamCity+Plugins

In this sample you will find
=============================
- TeamCity server-side only plugin
- Plugin version will be patched if building with IDEA build runner in TeamCity
- Run configuration to run/debug plugin under TeamCity (use `http://localhost:8111/bs`)
- pre-configured IDEA settings to support references to TeamCity
- Uses `$TeamCityDistribution$` IDEA path variable as path to TeamCity home (unpacked .tar.gz or .exe distribution)
- Bunch of libraries for most recent needed TeamCity APIs
- Module with TestNG tests that uses TeamCity Tests API

License
=======
You may do what ever you like with those sources. 
or I could also say the license is MIT.
