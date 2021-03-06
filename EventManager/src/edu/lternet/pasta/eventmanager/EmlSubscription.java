/*
 * $Date$
 * $Author$
 * $Revision$
 *
 * Copyright 2010 the University of New Mexico.
 *
 * This work was supported by National Science Foundation Cooperative Agreements
 * #DEB-0832652 and #DEB-0936498.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package edu.lternet.pasta.eventmanager;

import edu.lternet.pasta.common.EmlPackageId;
import edu.lternet.pasta.common.EmlPackageIdFormat;

/**
 * This class is used to represent <em>EML modification</em> event
 * subscriptions, which are associations between EML packageIds and URIs created
 * by particular users. Subscriptions can be persisted in a database using the
 * {@link SubscriptionRegsitry} class.
 *
 * <p>
 * Instances of this class are immutable, except in the following two ways. 
 * Subscription IDs are automatically generated when a subscription is first persisted, and
 * subscriptions are active upon their creation, but they can be irreversibly
 * inactivated using the method {@link #inactivate()}. The corresponding
 * data table row for a subscription object will not reflect this state change
 * until the object is persisted again.
 * </p>
 */
public class EmlSubscription {
	
	/* Instance variables */

    private Integer subscriptionId;
    private boolean active = true;
    private String creator;
    private String scope;
    private Integer identifier;
    private Integer revision;
    private SubscribedUrl url;
    
    
    /*Constructors */

    /**
     * Constructs an empty subscription. A no-arg constructor is required for
     * JPA Entities. Use the builder class to construct subscriptions
     * with content.
     */
    protected EmlSubscription() {}
    
    
    /* Instance methods */
    
    
    /*
     * Getters
     */

    /**
     * Returns the ID of this subscription. IDs are produced when subscriptions
     * are persisted in a database.
     *
     * @return the ID of this subscription if it has been persisted; {@code
     *         null} otherwise.
     */
    public Integer getSubscriptionId() {
        return subscriptionId;
    }

    
    /**
     * Indicates if this subscription is still active.
     * @return {@code true} if active; {@code false} if inactive.
     */
    public boolean isActive() {
        return active;
    }

    
    /**
     * Inactivates this subscription. Subsequent invocations of
     * {@link #isActive()} will return {@code false}. Inactivating more
     * than once has no effect.
     */
    public void inactivate() {
        active = false;
    }

    
    /**
     * Returns the ID of the user that created this subscription.
     *
     * @return the ID of the user that created this subscription.
     */
    public String getCreator() {
        return creator;
    }

    
    /**
     * Returns the scope of the EML packageId.
     *
     * @return the scope of the EML packageId.
     */
    public String getScope() {
        return scope;
    }

    /**
     * Returns the identifier of the EML packageId.
     *
     * @return the identifier of the EML packageId.
     */
    public Integer getIdentifier() {
        return identifier;
    }

    
    /**
     * Returns the revision of the EML packageId.
     *
     * @return the revision of the EML packageId.
     */
    public Integer getRevision() {
        return revision;
    }

    
    public EmlPackageId getPackageId() {
        return new EmlPackageId(scope, identifier, revision);    
    }

    
    public String getPackageIdStr() {
        EmlPackageIdFormat epf = new EmlPackageIdFormat();
        String packageIdStr = epf.format(getPackageId());
        return packageIdStr;
    }

    
    /**
     * Returns the subscribed URL.
     *
     * @return the subscribed URL.
     */
    public SubscribedUrl getUrl() {
        return url;
    }

    
    /*
     * Setters
     */
    
    /**
     * Sets the subscriptionId value
     * @param the id value to set
     */
    public void setSubscriptionId(Integer id) {
        this.subscriptionId = id;
    }

    
    /**
     * Sets the creator of the subscription.
     * @param creator the user ID of the creator.
     * @throws IllegalArgumentException if {@code creator} is {@code null}
     * or empty.
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    
    /**
     * Sets the scope
     * @param the scope of the packageId
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

    
    /**
     * Sets the identifier
     * @param the identifier value of the packageId
     */
    public void setIdentifier(Integer identifier) {
        this.identifier = identifier;
    }

    
    /**
     * Sets the EML packageId of the subscription.
     * @param packageId the EML packageId of the subscription.
     */
    public void setPackageId(EmlPackageId packageId) {
        if (packageId == null) {
            throw new IllegalArgumentException("null packageId");
        }
        this.scope = packageId.getScope();
        this.identifier = packageId.getIdentifier();
        this.revision = packageId.getRevision();
    }

    
    /**
     * Sets the revision
     * @param the revision value of the packageId
     */
    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    
    /**
     * Sets the URL of the subscription.
     * @param url the URL of the subscription.
     */
    public void setUrl(String url) {
        this.url = new SubscribedUrl(url);
    }

    
    /**
     * Returns an XML representation of this subscription.
     *
     * @return an XML string representation of this subscription.
     */
    public String toXML() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("  <subscription type=\"eml\">\n"));
        sb.append(String.format("    <id>%s</id>\n", getSubscriptionId()));
        sb.append(String.format("    <creator>%s</creator>\n", getCreator()));
        sb.append(String.format("    <packageId>%s</packageId>\n", getPackageIdStr()));
        sb.append(String.format("    <url>%s</url>\n", getUrl()));
        sb.append(String.format("  </subscription>\n"));
        return sb.toString();
    }

    
    /**
     * Returns a string representation of this subscription.
     *
     * @return a string representation of this subscription.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id=" + subscriptionId);
        sb.append(",active=" + active);
        sb.append(",creator=" + creator);
        sb.append(",packageId=" + scope + "." + identifier + "." + revision);
        sb.append(",uri=" + url);
        return sb.toString();
    }

}
