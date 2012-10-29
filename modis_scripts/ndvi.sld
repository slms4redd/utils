<?xml version="1.0" encoding="UTF-8"?>
<sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml" version="1.0.0">
    <sld:UserLayer>
        <sld:LayerFeatureConstraints>
            <sld:FeatureTypeConstraint/>
        </sld:LayerFeatureConstraints>
        <sld:UserStyle>
            <sld:Name>20120812</sld:Name>
            <sld:Title/>
            <sld:FeatureTypeStyle>
                <sld:Name>name</sld:Name>
                <sld:FeatureTypeName>Feature</sld:FeatureTypeName>
                <sld:Rule>
                    <sld:RasterSymbolizer>
                        <sld:Geometry>
                            <ogc:PropertyName>geom</ogc:PropertyName>
                        </sld:Geometry>
                        <sld:ColorMap>
                            <sld:ColorMapEntry color="#000080" opacity="1.0" quantity="0.0" label="Water/Mask"/>
                            <sld:ColorMapEntry color="#FFE2C9" opacity="1.0" quantity="78.0" label="Bare Soil"/>
                            <sld:ColorMapEntry color="#FFD396" opacity="1.0" quantity="1451.0" label=""/>
                            <sld:ColorMapEntry color="#FFFFB0" opacity="1.0" quantity="2353.0" label=""/>
                            <sld:ColorMapEntry color="#C9FFC9" opacity="1.0" quantity="2941.0" label="Sparse Veg"/>
                            <sld:ColorMapEntry color="#B0E6B0" opacity="1.0" quantity="3922.0" label=""/>
                            <sld:ColorMapEntry color="#8CD38C" opacity="1.0" quantity="4706.0" label="Light Veg"/>
                            <sld:ColorMapEntry color="#64B064" opacity="1.0" quantity="5686.0" label=""/>
                            <sld:ColorMapEntry color="#4B964B" opacity="1.0" quantity="6667.0" label=""/>
                            <sld:ColorMapEntry color="#327D32" opacity="1.0" quantity="7647.0" label="Medium Veg"/>
                            <sld:ColorMapEntry color="#196419" opacity="1.0" quantity="8235.0" label=""/>
                            <sld:ColorMapEntry color="#004B00" opacity="1.0" quantity="8980.0" label=""/>
                            <sld:ColorMapEntry color="#001E00" opacity="1.0" quantity="9412.0" label="Heavy Veg"/>
                        </sld:ColorMap>
                    </sld:RasterSymbolizer>
                </sld:Rule>
            </sld:FeatureTypeStyle>
        </sld:UserStyle>
    </sld:UserLayer>
</sld:StyledLayerDescriptor>

