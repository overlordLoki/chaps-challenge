export default function serialize(grid: Array<Array<string>>, width: number, height: number) {
    console.log(grid)
    const root = document.implementation.createDocument(null, "grid");
    root.documentElement.setAttribute("width", width.toString());
    root.documentElement.setAttribute("height", height.toString());

    for (const row in grid) {
        if (grid[row])
            for (const col in grid[row]) {
                if (grid[row][col]) {
                    const tile = grid[row][col];
                    const cell = root.createElement("cell");
                    let element = root.createElement(tile);
                    if (tile.includes("Key") || tile.includes("Lock") && tile !== "exitLock") {
                        element = root.createElement(tile.includes("Key") ? "key" : "lock");
                        element.setAttribute("color", tile.replace("Key", "").replace("Lock", "").toLowerCase());
                    }
                    cell.appendChild(element);
                    cell.setAttribute("x", col);
                    cell.setAttribute("y", row);
                    root.documentElement.appendChild(cell);
                }
            }
    }

    const xsltDoc = new DOMParser().parseFromString([
        // describes how we want to modify the XML - indent everything
        '<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform">',
        '  <xsl:strip-space elements="*"/>',
        '  <xsl:template match="para[content-style][not(text())]">', // change to just text() to strip space in text nodes
        '    <xsl:value-of select="normalize-space(.)"/>',
        '  </xsl:template>',
        '  <xsl:template match="node()|@*">',
        '    <xsl:copy><xsl:apply-templates select="node()|@*"/></xsl:copy>',
        '  </xsl:template>',
        '  <xsl:output indent="yes"/>',
        '</xsl:stylesheet>',
    ].join('\n'), 'application/xml');

    const xsltProcessor = new XSLTProcessor();
    xsltProcessor.importStylesheet(xsltDoc);
    const resultDoc = xsltProcessor.transformToDocument(root);
    const resultXml = new XMLSerializer().serializeToString(resultDoc);
    console.log(resultXml);
    return resultXml;
}