/*
 * Copyright (c) 2009 Stephan Schloepke and innoQ Deutschland GmbH
 *
 * Stephan Schloepke: http://www.schloepke.de/
 * innoQ Deutschland GmbH: http://www.innoq.com/
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jbasics.xml.xhtml;

import java.util.Map;

import org.jbasics.types.builders.MapBuilder;

public final class XHTMLStandardEntityMapper {
	public static final Map<String, Integer> XHTML_STANDARD_ENTITIY_CODEPOINT_MAP;
	public static final Map<String, String> XHTML_STANDARD_ENTITY_MAP;

	static {
		XHTML_STANDARD_ENTITIY_CODEPOINT_MAP = new MapBuilder<String, Integer>().immutable().put("nbsp", new Integer(160)) //
				.put("iexcl", new Integer(161)) //
				.put("cent", new Integer(162)) //
				.put("pound", new Integer(163)) //
				.put("curren", new Integer(164)) //
				.put("yen", new Integer(165)) //
				.put("brvbar", new Integer(166)) //
				.put("sect", new Integer(167)) //
				.put("uml", new Integer(168)) //
				.put("copy", new Integer(169)) //
				.put("ordf", new Integer(170)) //
				.put("laquo", new Integer(171)) //
				.put("not", new Integer(172)) //
				.put("shy", new Integer(173)) //
				.put("reg", new Integer(174)) //
				.put("macr", new Integer(175)) //
				.put("deg", new Integer(176)) //
				.put("plusmn", new Integer(177)) //
				.put("sup2", new Integer(178)) //
				.put("sup3", new Integer(179)) //
				.put("acute", new Integer(180)) //
				.put("micro", new Integer(181)) //
				.put("para", new Integer(182)) //
				.put("middot", new Integer(183)) //
				.put("cedil", new Integer(184)) //
				.put("sup1", new Integer(185)) //
				.put("ordm", new Integer(186)) //
				.put("raquo", new Integer(187)) //
				.put("frac14", new Integer(188)) //
				.put("frac12", new Integer(189)) //
				.put("frac34", new Integer(190)) //
				.put("iquest", new Integer(191)) //
				.put("Agrave", new Integer(192)) //
				.put("Aacute", new Integer(193)) //
				.put("Acirc", new Integer(194)) //
				.put("Atilde", new Integer(195)) //
				.put("Auml", new Integer(196)) //
				.put("Aring", new Integer(197)) //
				.put("AElig", new Integer(198)) //
				.put("Ccedil", new Integer(199)) //
				.put("Egrave", new Integer(200)) //
				.put("Eacute", new Integer(201)) //
				.put("Ecirc", new Integer(202)) //
				.put("Euml", new Integer(203)) //
				.put("Igrave", new Integer(204)) //
				.put("Iacute", new Integer(205)) //
				.put("Icirc", new Integer(206)) //
				.put("Iuml", new Integer(207)) //
				.put("ETH", new Integer(208)) //
				.put("Ntilde", new Integer(209)) //
				.put("Ograve", new Integer(210)) //
				.put("Oacute", new Integer(211)) //
				.put("Ocirc", new Integer(212)) //
				.put("Otilde", new Integer(213)) //
				.put("Ouml", new Integer(214)) //
				.put("times", new Integer(215)) //
				.put("Oslash", new Integer(216)) //
				.put("Ugrave", new Integer(217)) //
				.put("Uacute", new Integer(218)) //
				.put("Ucirc", new Integer(219)) //
				.put("Uuml", new Integer(220)) //
				.put("Yacute", new Integer(221)) //
				.put("THORN", new Integer(222)) //
				.put("szlig", new Integer(223)) //
				.put("agrave", new Integer(224)) //
				.put("aacute", new Integer(225)) //
				.put("acirc", new Integer(226)) //
				.put("atilde", new Integer(227)) //
				.put("auml", new Integer(228)) //
				.put("aring", new Integer(229)) //
				.put("aelig", new Integer(230)) //
				.put("ccedil", new Integer(231)) //
				.put("egrave", new Integer(232)) //
				.put("eacute", new Integer(233)) //
				.put("ecirc", new Integer(234)) //
				.put("euml", new Integer(235)) //
				.put("igrave", new Integer(236)) //
				.put("iacute", new Integer(237)) //
				.put("icirc", new Integer(238)) //
				.put("iuml", new Integer(239)) //
				.put("eth", new Integer(240)) //
				.put("ntilde", new Integer(241)) //
				.put("ograve", new Integer(242)) //
				.put("oacute", new Integer(243)) //
				.put("ocirc", new Integer(244)) //
				.put("otilde", new Integer(245)) //
				.put("ouml", new Integer(246)) //
				.put("divide", new Integer(247)) //
				.put("oslash", new Integer(248)) //
				.put("ugrave", new Integer(249)) //
				.put("uacute", new Integer(250)) //
				.put("ucirc", new Integer(251)) //
				.put("uuml", new Integer(252)) //
				.put("yacute", new Integer(253)) //
				.put("thorn", new Integer(254)) //
				.put("yuml", new Integer(255)) //
				// Symbol Entities
				.put("quot", new Integer(34)) //
				.put("amp", new Integer(38)) //
				.put("lt", new Integer(60)) //
				.put("gt", new Integer(62)) //
				.put("apos	", new Integer(39)) //
				.put("OElig", new Integer(338)) //
				.put("oelig", new Integer(339)) //
				.put("Scaron", new Integer(352)) //
				.put("scaron", new Integer(353)) //
				.put("Yuml", new Integer(376)) //
				.put("circ", new Integer(710)) //
				.put("tilde", new Integer(732)) //
				.put("ensp", new Integer(8194)) //
				.put("emsp", new Integer(8195)) //
				.put("thinsp", new Integer(8201)) //
				.put("zwnj", new Integer(8204)) //
				.put("zwj", new Integer(8205)) //
				.put("lrm", new Integer(8206)) //
				.put("rlm", new Integer(8207)) //
				.put("ndash", new Integer(8211)) //
				.put("mdash", new Integer(8212)) //
				.put("lsquo", new Integer(8216)) //
				.put("rsquo", new Integer(8217)) //
				.put("sbquo", new Integer(8218)) //
				.put("ldquo", new Integer(8220)) //
				.put("rdquo", new Integer(8221)) //
				.put("bdquo", new Integer(8222)) //
				.put("dagger", new Integer(8224)) //
				.put("Dagger", new Integer(8225)) //
				.put("permil", new Integer(8240)) //
				.put("lsaquo", new Integer(8249)) //
				.put("rsaquo", new Integer(8250)) //
				.put("euro", new Integer(8364)) //
				// Greek Letters
				.put("Alpha", new Integer(913)) //
				.put("Beta", new Integer(914)) //
				.put("Gamma", new Integer(915)) //
				.put("Delta", new Integer(916)) //
				.put("Epsilon", new Integer(917)) //
				.put("Zeta", new Integer(918)) //
				.put("Eta", new Integer(919)) //
				.put("Theta", new Integer(920)) //
				.put("Iota", new Integer(921)) //
				.put("Kappa", new Integer(922)) //
				.put("Lambda", new Integer(923)) //
				.put("Mu", new Integer(924)) //
				.put("Nu", new Integer(925)) //
				.put("Xi", new Integer(926)) //
				.put("Omicron", new Integer(927)) //
				.put("Pi", new Integer(928)) //
				.put("Rho", new Integer(929)) //
				.put("Sigma", new Integer(931)) //
				.put("Tau", new Integer(932)) //
				.put("Upsilon", new Integer(933)) //
				.put("Phi", new Integer(934)) //
				.put("Chi", new Integer(935)) //
				.put("Psi", new Integer(936)) //
				.put("Omega", new Integer(937)) //
				.put("alpha", new Integer(945)) //
				.put("beta", new Integer(946)) //
				.put("gamma", new Integer(947)) //
				.put("delta", new Integer(948)) //
				.put("epsilon", new Integer(949)) //
				.put("zeta", new Integer(950)) //
				.put("eta", new Integer(951)) //
				.put("theta", new Integer(952)) //
				.put("iota", new Integer(953)) //
				.put("kappa", new Integer(954)) //
				.put("lambda", new Integer(955)) //
				.put("mu", new Integer(956)) //
				.put("nu", new Integer(957)) //
				.put("xi", new Integer(958)) //
				.put("omicron", new Integer(959)) //
				.put("pi", new Integer(960)) //
				.put("rho", new Integer(961)) //
				.put("sigmaf", new Integer(962)) //
				.put("sigma", new Integer(963)) //
				.put("tau", new Integer(964)) //
				.put("upsilon", new Integer(965)) //
				.put("phi", new Integer(966)) //
				.put("chi", new Integer(967)) //
				.put("psi", new Integer(968)) //
				.put("omega", new Integer(969)) //
				.put("thetasym", new Integer(977)) //
				.put("upsih", new Integer(978)) //
				.put("piv", new Integer(982)) //
				// Mathematical and thechnical symbols
				.put("bull", new Integer(8226)) //
				.put("hellip", new Integer(8230)) //
				.put("prime", new Integer(8242)) //
				.put("Prime", new Integer(8243)) //
				.put("oline", new Integer(8254)) //
				.put("frasl", new Integer(8260)) //
				.put("weierp", new Integer(8472)) //
				.put("image", new Integer(8465)) //
				.put("real", new Integer(8476)) //
				.put("trade", new Integer(8482)) //
				.put("alefsym", new Integer(8501)) //
				.put("larr", new Integer(8592)) //
				.put("uarr", new Integer(8593)) //
				.put("rarr", new Integer(8594)) //
				.put("darr", new Integer(8595)) //
				.put("harr", new Integer(8596)) //
				.put("crarr", new Integer(8629)) //
				.put("lArr", new Integer(8656)) //
				.put("uArr", new Integer(8657)) //
				.put("rArr", new Integer(8658)) //
				.put("dArr", new Integer(8659)) //
				.put("hArr", new Integer(8660)) //
				.put("forall", new Integer(8704)) //
				.put("part", new Integer(8706)) //
				.put("exist", new Integer(8707)) //
				.put("empty", new Integer(8709)) //
				.put("nabla", new Integer(8711)) //
				.put("isin", new Integer(8712)) //
				.put("notin", new Integer(8713)) //
				.put("ni", new Integer(8715)) //
				.put("prod", new Integer(8719)) //
				.put("sum", new Integer(8721)) //
				.put("minus", new Integer(8722)) //
				.put("lowast", new Integer(8727)) //
				.put("radic", new Integer(8730)) //
				.put("prop", new Integer(8733)) //
				.put("infin", new Integer(8734)) //
				.put("ang", new Integer(8736)) //
				.put("and", new Integer(8743)) //
				.put("or", new Integer(8744)) //
				.put("cap", new Integer(8745)) //
				.put("cup", new Integer(8746)) //
				.put("int", new Integer(8747)) //
				.put("there4", new Integer(8756)) //
				.put("sim", new Integer(8764)) //
				.put("cong", new Integer(8773)) //
				.put("asymp", new Integer(8776)) //
				.put("ne", new Integer(8800)) //
				.put("equiv", new Integer(8801)) //
				.put("le", new Integer(8804)) //
				.put("ge", new Integer(8805)) //
				.put("sub", new Integer(8834)) //
				.put("sup", new Integer(8835)) //
				.put("nsub", new Integer(8836)) //
				.put("sube", new Integer(8838)) //
				.put("supe", new Integer(8839)) //
				.put("oplus", new Integer(8853)) //
				.put("otimes", new Integer(8855)) //
				.put("perp", new Integer(8869)) //
				.put("sdot", new Integer(8901)) //
				.put("lceil", new Integer(8968)) //
				.put("rceil", new Integer(8969)) //
				.put("lfloor", new Integer(8970)) //
				.put("rfloor", new Integer(8971)) //
				.put("lang", new Integer(9001)) //
				.put("rang", new Integer(9002)) //
				.put("loz", new Integer(9674)) //
				.put("spades", new Integer(9824)) //
				.put("clubs", new Integer(9827)) //
				.put("hearts", new Integer(9829)) //
				.put("diams", new Integer(9830)) //
				.build(); //

		final MapBuilder<String, String> stringEntities = new MapBuilder<String, String>().immutable();
		for (final Map.Entry<String, Integer> entry : XHTMLStandardEntityMapper.XHTML_STANDARD_ENTITIY_CODEPOINT_MAP.entrySet()) {
			stringEntities.put(entry.getKey(), new String(new int[] { entry.getValue().intValue() }, 0, 1));
		}
		XHTML_STANDARD_ENTITY_MAP = stringEntities.build();
	}

	public static String resolveEntity(final String entityName) {
		return XHTMLStandardEntityMapper.XHTML_STANDARD_ENTITY_MAP.get(entityName);
	}

	private XHTMLStandardEntityMapper() {
		// To disallow instanciation
	}

}
