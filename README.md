PdfExtractor
===================================================

`PdfExtractor` is a library to obtain all the resources of a pdf.

© 2018 Jorge Galán - OEG-UPM. Available under Apache License 2.0. See [`LICENSE`](LICENSE).

## Features

- Extract your text and images in a PDF, thanks to [PDFBox](https://github.com/apache/pdfbox) technology.
- Extract your tables in a PDF, thanks to [Tabula](https://github.com/tabulapdf/tabula-java) technology.
- Choose the parts to extract from your PDF, by bookmarks, by pages or all PDF. 

## Download

Download a version of the PdfExtractor's jar from our [releases page](../../releases).

## Usage

`PdfExtractor` provides a command line application:

```
$java -jar PdfExtractor.jar --help 
usage: PDFExtractor [-b <NUMBERS>] [-f] [-h] [-i <input PDF or FOLDER>]
       [-o <output FOLDER>] [-p <NUMBERS>] [-r]
Mised argument
 -b,--bookmark <NUMBERS>            [OPTIONAL] ¡NOT AT SAME THAN -p! By
                                    default, the extractor extract all of
                                    them.
                                    If the PDF has BOOKMARKS, we extract
                                    all content from selected. Using comma
                                    separated or list of ranges to
                                    listExamples: --bookmark 1-3,5-7,
                                    --bookmark 3.
 -f,--fix                           [EXPERIMENTAL] Force PDF to be
                                    extracted adjunting words, deleting
                                    files, deleting footers, .. By
                                    default, disabled
 -h,--help                          Indicate how yo use the program.
 -i,--input <input PDF or FOLDER>   [REQUIRED] Absolute Pdf or folder with
                                    PDF location path. Ex:
                                    /Users/thoqbk/table.pdf
 -o,--output <output FOLDER>        Absolute output file. By default the
                                    folder on i or the parent. Ex:
                                    /Users/thoqbk/results
 -p,--pages <NUMBERS>               [OPTIONAL] ¡NOT AT SAME THAN -p! By
                                    default, the extractor extract all of
                                    them.
                                    Using comma separated or list of
                                    ranges to list to select
                                    pagesExamples: --pages 1-3,5-7,
                                    --pages 3.
 -r,--resources                     Try to extract all resources from PDF
                                    (text, image and tables). By default,
                                    disabled

```
The option --fix try to join parts of separate words together, remove footers and headers, and remove tables from the final text

## Building from Source

Clone this repo and run:

```
mvn clean compile assembly:single
```

Then, get your own version of the jar in the project's `target` folder.

<a title="OEG Laboratory" href="http://www.oeg-upm.net/" target="_blank"><img alt="OEG Laboratory" src="http://stars4all.eu/wp-content/uploads/2016/10/OEG.png" width="400" height="400"></a>
<a title="STARS4ALL" href="http://stars4all.eu" target="_blank"><img alt="STARS4ALL" src="http://linkeddata4.dia.fi.upm.es/wordpress-new/wp-content/uploads/2016/12/logo_dark.png" width="400" height="400"></a>
