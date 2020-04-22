# series-subtitle-renamer
Utility to rename TV series subtitles files 

## How to use

It works by command line, using these parameters:

| Name               | Type          | Optional    | Defaut               | Description |
| ---                | :---          | :---        | :--                  | ---         |
| path               | ```String```  | ```false``` | *empty*              | The path to folder where files are **mandatory** |
| backupFiles        | ```boolean``` | ```true```  | ```false```          | Indicate if is necessary to back up files before rename<br>Ex: ```-DbackupFiles=false``` |
| dryRun             | ```boolean``` | ```true```  | ```false```          | If is ```true```, just shows the changes to be made on the console<br>Ex: ```-DdryRun=true``` |
| episodeIdentifier  | ```String```  | ```true```  | ```"S\d{2}E\d{2}"``` | A regular expression to identify episode numbers on subtitle and video files<br>Ex: ```-DepisodeIdentifier=S\d{2}E\d{2}``` |
| overwrite          | ```boolean``` | ```true```  | ```false```          | If already exists a file with same name and value is ```true```, overwrite. If value is ```false```, skip.<br><br>**Important!** Case ```true```, the overwritten file will be always backed up.<br>Ex: ```-Doverwrite=true``` |
| subtitleExtensions | ```String```  | ```true```  | ```"srt,sub"```      | Subtitle file extensions to be used. It's a comma separated list of extensions (*without dot*). **If there are blank spaces, it must be enclosed in quotation marks.**<br>Ex: ```-DsubtitleExtensions="srt, sub"``` |
| suffix             | ```String```  | ```true```  | *empty*              | Suffix to be added to the subtitle file name.<br>Ex: ```-Dsuffix=.por``` |
| videoExtensions    | ```String```  | ```true```  | * [see list below](#video-extensions)     | Video file extensions to be used. It's a comma separated list of extensions (*without dot*). **If there are blank spaces, it must be enclosed in quotation marks.**<br>Ex: ```-DvideoExtensions="mkv, avi"``` |

### Video extensions
| Name | Extension(s) |
| --- | --- |
| WebM | webm |
| Matroska | mkv |
| Flash Video | flv |
| Vob | vob |
| Ogg | ogv, ogg |
| Dirac | drc |
| Multiple-image Network Graphics | mng |
| Audio Video Interleave | avi |
| MPEG Transport Stream | mts, m2ts, ts |
| QuickTime File Format | mov, qt |
| Windows Media Video | wmv |
| Raw video format | yuv |
| RealMedia | rm |
| RealMedia Variable Bitrate | rmvb |
| Advanced Systems Format | asf |
| AMV video format | amv |
| MPEG-1 | mpg, mp2, mpeg, mpe, mpv |
| MPEG-2 | mpg, mpeg, m2v |
| MPEG-4 Part 14 | mp4, m4p, m4v |
| M4V | m4v |
| SVI | svi |
| 3GPP | 3gp |
| 3GPP2 | 3g2 |
| Material Exchange Format | mxf |
| ROQ | roq |
| Nullsoft Streaming Video | nsv |

