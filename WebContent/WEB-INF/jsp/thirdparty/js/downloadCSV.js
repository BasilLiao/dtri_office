window.downloadCSV = function(ary, fileName){
    var r, c, tmp, txt, resault = [];
    for(r = 0 ; r < ary.length ; r++){
        tmp = [];
        for(c = 0 ; c < ary[r].length ; c++){
            txt = (typeof(ary[r][c]) != 'undefined')?ary[r][c].toString():'';
            txt = txt.replace(/"/g,'""');
            if(resault.length === 0 && tmp.length === 0){
                tmp.push(txt);
            }else{
                tmp.push('"'+txt+'"');
            }
        }
        resault.push(tmp.join(","));
    }
    resault = resault.join("\n");
    var blob = new Blob(["\uFEFF"+resault], {type: "text/csv;charset=utf-8"});
    window.downloadFile(blob, fileName + '.csv');
}
window.downloadFile = function(content, fileName){
    if(typeof(window.saveAs) != 'function'){
        var url = "FileSaver.min.js";
        $.getScript(url, function(){
            window.saveAs(content, fileName);
        });
    }else{
        window.saveAs(content, fileName);
    }
}
window.table2Array = function($table, trStart, trEnd, tdStart, tdEnd){
    var maxTd = 0,
    trs = $table.get(0).rows;
    for(var i = 0 ; i < trs.length ; i++){
        if(maxTd < trs[i].cells.length)maxTd = trs[i].cells.length;
    }
    
    var isNum = function(n){return (typeof(n) == 'number')};
    if(!isNum(trStart))trStart = 0;
    if(!isNum(trEnd))trEnd = trs.length - 1;
    if(trEnd >= trs.length)trEnd = trs.length - 1;
    if(!isNum(tdStart))tdStart = 0;
    if(!isNum(tdEnd))tdEnd = maxTd - 1;
    if(tdEnd >= maxTd)tdEnd = maxTd - 1;

    var trIndex, tdIndex, tr, tds, $td, trAry, text,
    ary = []
    for(trIndex = trStart ; trIndex <= trEnd ; trIndex++){
        tr = trs[trIndex];
        tds = tr.cells;
        trAry = [];
        for(tdIndex = tdStart ; tdIndex <= tdEnd ; tdIndex++){
            $td = $(tds[tdIndex]);
            text = $td.text().trim();
            text = text.replace(/<br[^>]*>/ig, "\\n");
            trAry.push(text);
        }
        ary.push(trAry);
    }
    return ary;
}