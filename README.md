Запуск:

1. Собрать толстый jar через Gradle
2. Вызвать утилиту через cmd:
```shell
"C:\Users\save2\.jdks\graalvm-ce-23.0.2\bin\java.exe" ^
-jar "C:\Users\save2\_Private\Work\gpx-data-copier\build\libs\gpx-data-copier-1.0-SNAPSHOT.jar" ^
-fileFrom "C:\Users\save2\Desktop\1918_2GC40.gpx" ^
-fileTo "C:\Users\save2\Desktop\1918_2GC40_no_hr.gpx"
```
Или через powershell:
```shell
& "C:\Users\save2\.jdks\graalvm-ce-23.0.2\bin\java.exe" `
-jar "C:\Users\save2\_Private\Work\gpx-data-copier\build\libs\gpx-data-copier-1.0-SNAPSHOT.jar" `
-fileFrom "C:\Users\save2\Desktop\1918_2GC40.gpx" `
-fileTo "C:\Users\save2\Desktop\1918_2GC40_no_hr.gpx"
```