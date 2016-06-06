Welcome to this very quick and dirty mock-up, which might sometimes even work.

Place ePrescriptions in epstore and Patient Summaries in psstore.
For each eP and each PS there MUST be two versions with the same file name, 
but with different extensions: .xml and .pdf. 

Do NOT put PS/eP PDF documents wrapped in XML. The mock will wrap these automatically.

OIDs of the documents are automatically modified: .1 is added in the end for XML and .2 for PDF.

A limitation: the first element of the XML document must be ClinicalDocument. Remove any
references to stylesheets etc. <?xml version="1.0" encoding="UTF-8"?> is naturally ok.

A limitation: there should not be a name used for default namespace. e.g. no
<ns:id root="1234" extension="5678"/>
but
<id root="1234" extension="5678"/>