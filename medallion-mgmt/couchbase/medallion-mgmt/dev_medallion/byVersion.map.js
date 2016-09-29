function (doc, meta) {
if(doc._class == "com.carnival.mm.domain.Medallion" && doc.hardwareId && doc.__version){
emit(doc.hardwareId, doc);
}
}