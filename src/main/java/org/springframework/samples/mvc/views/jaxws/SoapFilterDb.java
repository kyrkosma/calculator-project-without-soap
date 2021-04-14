
package org.springframework.samples.mvc.views.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for soapFilterDb complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="soapFilterDb">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="filterBy" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="equals" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "soapFilterDb", propOrder = {
    "filterBy",
    "equals"
})
public class SoapFilterDb {

    @XmlElement(required = true)
    protected String filterBy;
    @XmlElement(required = true)
    protected String equals;

    /**
     * Gets the value of the filterBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFilterBy() {
        return filterBy;
    }

    /**
     * Sets the value of the filterBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFilterBy(String value) {
        this.filterBy = value;
    }

    /**
     * Gets the value of the equals property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEquals() {
        return equals;
    }

    /**
     * Sets the value of the equals property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEquals(String value) {
        this.equals = value;
    }

}
