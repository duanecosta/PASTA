/*
 * Copyright 2011-2014 the University of New Mexico.
 *
 * This work was supported by National Science Foundation Cooperative
 * Agreements #DEB-0832652 and #DEB-0936498.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */

package edu.lternet.pasta.common.security.authorization;

import edu.lternet.pasta.common.security.authentication.IdentityFactory;
import edu.lternet.pasta.common.security.authorization.Rule.Permission;
import edu.lternet.pasta.common.security.authentication.jaxb.Token;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @author servilla
 *
 * Create a PASTA access matrix object based on either an XML access element or
 * an existing access matrix rule set.
 */
public class AccessMatrix {

	/*
	 * Class Fields
	 */

	/*
	 * Instance Fields
	 */

	private String order = "allowFirst";
	private ArrayList<Rule> ruleList;

	// Declare hash tables of "allow" and "deny" rules
	private Hashtable<String, Rule> allowRules = new Hashtable<String, Rule>();
	private Hashtable<String, Rule> denyRules  = new Hashtable<String, Rule>();

	/*
	 * Constructors
	 */

	/**
	 * Generate access matrix from XML access element.
	 *
	 * @param ae The XML access element.
	 * @throws InvalidPermissionException
	 */
	public AccessMatrix(String ae) throws InvalidPermissionException {

			AccessElement accessElement = new AccessElement(ae);
			this.ruleList = accessElement.getRuleList();

	}

	/**
	 * Generate access matrix from existing rule list.
	 *
	 * @param ruleList The existing access matrix rule list.
	 */
	public AccessMatrix(ArrayList<Rule> ruleList) {

		this.ruleList = ruleList;

	}

	/*
	 * Class Methods
	 */

	/**
	 * Determine if the principal user or any of their group affiliations are
	 * authorized for the requested permission.
	 *
	 * @param token The authentication token identifying the principal user and
	 *                  their group affiliations
	 * @param owner The owner of the resource being requested
   * @param authSystem The authentication system used to identify the owner
	 * @param permission The requested permission
	 * @return The assertion of whether the principal user is authorized to access
	 *         the resource at the requested permission.
	 */
	public boolean isAuthorized(Token token, String owner, String authSystem, Permission permission) {

		boolean isAuthorized = false;

    // Test identity match to resource owner, who does not need to be in the
    // rule
    for (Token.Identity identity: token.getIdentity()) {
      String id = identity.getId();
      if (id.equals(Token.Identity.LOGIN) || id.equals(Token.Identity.MAP)) {
        String uid = identity.getIdentifier();
        String idP = identity.getProvider();
        if (uid.equalsIgnoreCase(owner) && idP.equalsIgnoreCase(authSystem))
          return true; // Owner has full access to resource
      }
    }

		if (this.ruleList != null) {

			// Build "allow"/"deny" rule hash tables
			for (int i = 0; i < this.ruleList.size(); i++) {

				Rule rule = this.ruleList.get(i);

				if (rule.getAccessType().equals(AccessElement.RULE_ALLOW)) {
					this.order = rule.getOrder();
					// Set hash key to lower case to simulate case insensitivity.
					this.allowRules.put(rule.getPrincipal().toLowerCase(), rule);
				} else { // Otherwise, "deny".
					this.order = rule.getOrder();
					// Set hash key to lower case to simulate case insensitivity.
					this.denyRules.put(rule.getPrincipal().toLowerCase(), rule);
				}

			}

			// Begin the process to determine if there exists an "allow" rule that
			// allows access to the principal or one of their groups or public.
      for (Token.Identity identity: token.getIdentity()) {
      	if (isAllowed(identity, permission)) {
          isAuthorized = true;
          break;
        }
			}

			// If order is "allowFirst" and the principal is temporarily authorized,
			// see if there are any "deny" rules that block access.
			if (this.order.equals(AccessElement.ALLOW_FIRST) && isAuthorized) {
        for (Token.Identity identity: token.getIdentity()) {
          if (isDenied(identity, permission)) {
            isAuthorized = false;
            break;
          }
        }
			}

			// If not authorized, test if "public" can access the resource
			if (!isAuthorized) {
        IdentityFactory.GlobalId gid = IdentityFactory.GlobalId.PUBLIC;
        Token.Identity identity = IdentityFactory.getGlobalIdentity(gid);
				if (isAllowed(identity, permission)) {
					isAuthorized = true;
				}
			}

		}

		return isAuthorized;

	}

	/**
	 * Determine whether the Identity is allowed access at the requested
	 * permission.
	 *
	 * @param identity Token identity.
	 * @param permission Requested permission.
	 * @return Boolean value of whether the Identity is allowed access to the
	 *         resource at the requested permission.
	 */
	private boolean isAllowed(Token.Identity identity, Permission permission){

		boolean isAllowed = false;
    String uid = identity.getIdentifier().toLowerCase();

		if (allowRules.containsKey(uid)) { // User identifier in hash table

			Rule rule = allowRules.get(uid);
      String idP = identity.getProvider();

      // Confirm matching identity provider
      if (idP.equalsIgnoreCase(rule.getAuthSystem()) || idP.equals(IdentityFactory.GLOBAL)) {
        // Determine if requested permission is less than or equal
        // to the rule permission
        if (permission.getRank() <= rule.getPermission().getRank()) {
          isAllowed = true;
        }
      }
		}

		return isAllowed;

	}

	/**
	 * Determine whether the principal identified is denied access at the requested
	 * permission.
	 *
	 * @param identity Token identity
	 * @param permission Requested permission.
	 * @return Boolean value of whether the Identity is denied access to the
	 *         resource at the requested permission.
	 */
	private boolean isDenied(Token.Identity identity, Permission permission){

		boolean isDenied = false;
    String uid = identity.getIdentifier().toLowerCase();

		if (denyRules.containsKey(uid)) {  // User identifier in hash table.

			Rule rule = denyRules.get(uid);
      String idP = identity.getProvider();

      // Confirm matching identity provider
      if (idP.equalsIgnoreCase(rule.getAuthSystem()) || idP.equals(IdentityFactory.GLOBAL)) {
        // Determine if requested permission is greater or equal
        // to the rule permission
        if (permission.getRank() >= rule.getPermission().getRank()) {
          isDenied = true;
        }
      }
		}

		return isDenied;

	}

	/**
	 * Return the rule list of the access matrix object.
	 *
	 * @return The rule list as an array list.
	 */
	public ArrayList<Rule> getRuleList() {

		return this.ruleList;

	}

}
