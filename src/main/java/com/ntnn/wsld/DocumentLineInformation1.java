
package com.ntnn.wsld;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for DocumentLineInformation1 complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="DocumentLineInformation1">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="Id" type="{urn:iso:std:iso:20022:tech:xsd:pacs.008.001.07}DocumentLineIdentification1" maxOccurs="unbounded"/>
 *         <element name="Desc" type="{urn:iso:std:iso:20022:tech:xsd:pacs.008.001.07}Max2048Text" minOccurs="0"/>
 *         <element name="Amt" type="{urn:iso:std:iso:20022:tech:xsd:pacs.008.001.07}RemittanceAmount3" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentLineInformation1", propOrder = {
    "id",
    "desc",
    "amt"
})
public class DocumentLineInformation1 {

    @XmlElement(name = "Id", required = true)
    protected List<DocumentLineIdentification1> id;
    @XmlElement(name = "Desc")
    protected String desc;
    @XmlElement(name = "Amt")
    protected RemittanceAmount3 amt;

    /**
     * Gets the value of the id property.
     * 
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the id property.</p>
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DocumentLineIdentification1 }
     * </p>
     * 
     * 
     * @return
     *     The value of the id property.
     */
    public List<DocumentLineIdentification1> getId() {
        if (id == null) {
            id = new ArrayList<>();
        }
        return this.id;
    }

    /**
     * Gets the value of the desc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Sets the value of the desc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDesc(String value) {
        this.desc = value;
    }

    /**
     * Gets the value of the amt property.
     * 
     * @return
     *     possible object is
     *     {@link RemittanceAmount3 }
     *     
     */
    public RemittanceAmount3 getAmt() {
        return amt;
    }

    /**
     * Sets the value of the amt property.
     * 
     * @param value
     *     allowed object is
     *     {@link RemittanceAmount3 }
     *     
     */
    public void setAmt(RemittanceAmount3 value) {
        this.amt = value;
    }

}
