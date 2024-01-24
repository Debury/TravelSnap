package cz.mendelu.pef.xchomo.travelsnap.mock

import cz.mendelu.pef.xchomo.travelsnap.model.Geometry
import cz.mendelu.pef.xchomo.travelsnap.model.Location
import cz.mendelu.pef.xchomo.travelsnap.model.Photo
import cz.mendelu.pef.xchomo.travelsnap.model.Place


class PlaceMockFactory {

    companion object {

        fun createSamplePlace(): Place {
            return Place(
                id = "1",
                address = "Sample Address",
                name = "Sample Place",
                rating = 4.5,
                geometry = Geometry(
                    location = Location(
                        lat = 0.0,
                        lng = 0.0
                    )
                ),
                photos = listOf(Photo(
                    photoReference = "Sample Photo Reference",
                )),
                selectedDate = "2022-01-01",
                description = "Sample description",
                time = System.currentTimeMillis(),
                selectedIconId = 1
            )
        }

        fun createNullPlace(): Place {
            return Place(
                id = "",
                address = null,
                name = null,
                rating = null,
                geometry = null,
                photos = null,
                selectedDate = null,
                description = null,
                time = null,
                selectedIconId = null
            )
        }

        fun createMinimalPlace(): Place {
            return Place(
                id = "2",
                name = "Minimal Place",
                address = null,
                rating = null,
                geometry = null,
                photos = null,
                selectedDate = null,
                description = null,
                time = null,
                selectedIconId = null
            )
        }


        fun createCustomPlace(
            id: String,
            name: String?,
            address: String?,
            rating: Double?,
            geometry: Geometry?,
            photos: List<Photo>?,
            selectedDate: String?,
            description: String?,
            time: Long?,
            selectedIconId: Int?
        ): Place {
            return Place(
                id = id,
                address = address,
                name = name,
                rating = rating,
                geometry = geometry,
                photos = photos,
                selectedDate = selectedDate,
                description = description,
                time = time,
                selectedIconId = selectedIconId
            )
        }
    }
}
