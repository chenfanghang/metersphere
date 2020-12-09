package io.metersphere.api.dto.definition.request.configurations;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import io.metersphere.api.dto.definition.request.MsTestElement;
import io.metersphere.api.dto.definition.request.ParameterConfig;
import io.metersphere.api.dto.scenario.KeyValue;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.jmeter.protocol.http.control.Header;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.collections.HashTree;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@JSONType(typeName = "HeaderManager")
public class MsHeaderManager extends MsTestElement {

    private String type = "HeaderManager";
    @JSONField(ordinal = 10)
    private List<KeyValue> headers;

    public void toHashTree(HashTree tree, List<MsTestElement> hashTree, ParameterConfig config) {
        HeaderManager headerManager = new HeaderManager();
        headerManager.setEnabled(true);
        headerManager.setName(this.getName() + "Headers");
        headerManager.setProperty(TestElement.TEST_CLASS, HeaderManager.class.getName());
        headerManager.setProperty(TestElement.GUI_CLASS, SaveService.aliasToClass("HeaderPanel"));
        headers.stream().filter(KeyValue::isValid).filter(KeyValue::isEnable).forEach(keyValue ->
                headerManager.add(new Header(keyValue.getName(), keyValue.getValue()))
        );
        final HashTree headersTree = tree.add(headerManager);
        if (CollectionUtils.isNotEmpty(hashTree)) {
            hashTree.forEach(el -> {
                el.toHashTree(headersTree, el.getHashTree(), config);
            });
        }
    }
}