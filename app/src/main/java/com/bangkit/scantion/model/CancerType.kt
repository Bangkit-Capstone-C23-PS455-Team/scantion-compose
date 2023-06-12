package com.bangkit.scantion.model

class CancerType(
    val displayName: String,
    val fullName: String,
    val desc: String,
    val symptom: String,
    val type: String,
    val note: String
) {
    companion object {
        val listKey = listOf("Benign", "Melanoma", "Normal")
        fun getData(): Map<String, CancerType> {
            return mapOf(
                "Benign" to
                        CancerType(
                            displayName = "Benign",
                            fullName = "Benign / tumor (non cancerous)",
                            desc = "Benign refers to a non-cancerous condition or growth. Unlike malignant (cancerous) tumors, benign tumors do not invade nearby tissues or spread to other parts of the body. They tend to grow slowly and remain localized to the site where they originated.",
                            symptom = "1. Non-invasive: Benign tumors do not invade surrounding tissues or organs. They typically grow in a contained manner and do not spread to other areas of the body.\n" +
                                    "\n" +
                                    "2. Well-defined borders: Benign tumors often have distinct and well-defined borders, which can help differentiate them from malignant tumors.\n" +
                                    "\n" +
                                    "3. Slow growth: Benign tumors tend to grow at a slower pace compared to malignant tumors. Their growth rate may vary depending on the type and location of the tumor.\n" +
                                    "\n" +
                                    "4. Encapsulated: Some benign tumors are encapsulated, meaning they are surrounded by a fibrous capsule that separates them from the surrounding tissues.\n" +
                                    "\n" +
                                    "5. Normal cell appearance: Under a microscope, cells in benign tumors generally resemble normal cells. They do not exhibit significant cellular abnormalities or abnormal cell division.\n" +
                                    "\n" +
                                    "6. No metastasis: Benign tumors do not have the ability to metastasize or spread to other parts of the body through the bloodstream or lymphatic system.",
                            type = "1. Nevus (NV): Nevus refers to a benign mole or birthmark on the skin. While most nevi are harmless, certain types, such as atypical or dysplastic nevi, have a slightly increased risk of developing into melanoma.\n" +
                                    "\n" +
                                    "2. Basal Cell Carcinoma (BCC): BCC is the most common type of skin cancer, but it is not a form of melanoma. It typically develops in the basal cells of the skin's outermost layer and is commonly caused by prolonged exposure to the sun's UV rays.\n" +
                                    "\n" +
                                    "3. Actinic Keratosis (AKIEC): Actinic Keratosis, also known as solar keratosis, is a precancerous condition that appears as rough, scaly patches on sun-exposed areas of the skin. While AKIEC is not melanoma, it is important to monitor and treat these lesions as they can progress to squamous cell carcinoma, a type of skin cancer.\n" +
                                    "\n" +
                                    "4. Benign Keratosis-like Lesions (BKL): This term generally encompasses a variety of benign skin lesions that resemble keratosis or rough patches on the skin. Examples may include seborrheic keratosis or keratoacanthoma. These lesions are non-cancerous and do not typically progress to melanoma.\n" +
                                    "\n" +
                                    "5. Dermatofibroma (DF): Dermatofibromas are common benign skin growths that usually develop as small, firm bumps on the skin. They often appear on the legs and are not related to melanoma.\n" +
                                    "\n" +
                                    "6. Vascular Lesions (VASC): Vascular lesions include various types of abnormalities in blood vessels, such as hemangiomas, venous malformations, or vascular tumors. These lesions are not melanoma but rather involve the blood vessels within the skin.",
                            note = "It's important to note that although benign tumors are generally not life-threatening, their growth or presence in certain locations can still cause health issues. Some benign tumors, depending on their size or location, can exert pressure on nearby structures, leading to symptoms or complications. In such cases, medical intervention may be required to address the tumor. However, regular monitoring and follow-up may be recommended to ensure the tumor remains benign and does not undergo any significant changes over time.And SCANTION recommended all the user who has that Benign Symptoms to go the nearest Hospital or Doctor to get the spesific and detail information about it."
                        ),
                "Melanoma" to
                        CancerType(
                            displayName = "Melanoma",
                            fullName = "Melanoma (Maglinant)",
                            desc = "Melanoma, also called malignant melanoma, is a cancer that usually starts in the skin. It can start in a mole or in normal-looking skin. A melanoma is a tumor produced by the malignant transformation of melanocytes. ",
                            symptom = "1. Irregular Moles: A new mole or an existing mole that undergoes changes in size, shape, color, or texture may be a warning sign. Melanomas often have uneven or irregular borders and may be larger than a pencil eraser.\n" +
                                    "\n" +
                                    "2. Asymmetry: Melanomas are typically asymmetric, meaning one half of the mole or lesion does not match the other half.\n" +
                                    "\n" +
                                    "3. Varied Colors: Melanomas may have multiple colors or shades within the same lesion, including areas of brown, black, blue, red, or white.\n" +
                                    "\n" +
                                    "4. Diameter: Melanomas are usually larger in diameter compared to regular moles. They are generally more significant than 6 millimeters (about the size of a pencil eraser), but they can be smaller as well.\n" +
                                    "\n" +
                                    "5. Evolving or Changing: Any changes in the size, shape, color, elevation, or other characteristics of a mole or pigmented area should be evaluated. This includes itching, bleeding, crusting, or ulceration of the mole.\n" +
                                    "\n" +
                                    "6. Spreading: Melanomas may extend beyond the border of a mole or lesion, growing into the surrounding skin.\n" +
                                    "\n" +
                                    "7. Sensation: Some individuals may experience tenderness, pain, or itching in the affected area.",
                            type = "",
                            note = "It is important to note that not all melanomas display these symptoms, and some melanomas may lack pigmentation and appear as pink, red, or flesh-colored lesions. It's crucial to regularly examine your skin and consult a dermatologist if you notice any concerning changes or abnormalities in your moles or skin. Early detection and treatment greatly increase the chances of successful outcomes in melanoma cases. So if you are detcted has Melanoma Skin cancer so, SCANTION suggest you to go to the neariest Doctor or Hospital."
                        ),
                "Normal" to
                        CancerType(
                            displayName = "Normal",
                            fullName = "Normal Skin",
                            desc = "1. Normal Skin has clear area and there's no scars at all. Normal skin appears smooth, even-toned, and consistent in color. It does not have any unusual growths, discoloration, or irregular borders.\n" +
                                    "\n" +
                                    "2. Normal skin has a balanced pigmentation level. It may have variations in skin tone due to factors like sun exposure, ethnicity, or natural variations, but it does not have large or irregularly shaped pigmented areas that are indicative of melanoma.\n" +
                                    "\n" +
                                    "3. Moles are common on normal skin and are usually harmless. Normal moles are typically small, round or oval-shaped, and have a consistent color throughout. They may be flat or slightly raised but are usually not larger than the diameter of a pencil eraser.",
                            symptom = "",
                            type = "",
                            note = ""
                        ),
            )
        }
    }
}